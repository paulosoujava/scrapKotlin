package com.oliveira.scrapy


import org.jsoup.Jsoup

data class Match(
    val rodada: String,
    val dataHora: String,
    val jogo: String,
    val link: String,
    val timeCasaSigla: String,
    val escudoTimeCasa: String,
    val timeVisitanteSigla: String,
    val escudoTimeVisitante: String,
    val placar: String?,
    val local: String?
)

suspend fun getGames(year: String): Pair<List<Match>, String> {

    val jogos = mutableListOf<Match>()
    val indices = mutableListOf<Int>()
    var title: String? = null

    val response = connect().table(year)
    if (response.isSuccessful && response.body() != null) {
        val doc = Jsoup.parse(response.body()!!)
        val metaTag = doc.select("h2.title.uppercase")
        title = metaTag.text()

        val elements = doc.select("div.swiper-wrapper div.swiper-slide")


        for (element in elements) {
            val slideIndex = element.attr("data-slide-index")
            println("Slide Index: $slideIndex")
            val divElement = doc.selectFirst("div[data-slide-index=\"$slideIndex\"].swiper-slide")
            val elementsList = divElement?.select("ul.list-unstyled li")
            if (elementsList != null) {
                for (ul in elementsList) {
                    val match = parseHtmlToMatch(ul.html())
                    if (match != null) {
                        val m = match.copy(rodada = (slideIndex.toInt() + 1).toString())
                        jogos.add(m)
                        println("Match: $m")
                    }
                }
            }
            //indices.add(slideIndex.toInt())
        }


    }
    return Pair(jogos, title ?: "")

}


fun parseHtmlToMatch(html: String): Match? {
    try {
        val doc = Jsoup.parse(html)

        val rodada = doc.select("h3.text-center").text()
        val partidaDesc = doc.select("span.partida-desc").text()
        val links = doc.select("a[href]").eachAttr("href")
        val escudos = doc.select("div.time > span.time-sigla").eachText() zip
                doc.select("div.time > img").eachAttr("src")

        val (dataHora, jogo) = Regex("([A-Z][a-z]+, \\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}) - Jogo: (\\d+)").find(partidaDesc)!!.destructured
        val (timeCasaSigla, escudoTimeCasa) = escudos.first()
        val (timeVisitanteSigla, escudoTimeVisitante) = escudos.last()

        val strongElement = doc.select("span.bg-blue.color-white.label-2").firstOrNull()

        val placar = strongElement?.text()

        val local = doc.select("span.partida-desc.text-1.color-lightgray.block.uppercase.text-center").last()?.text()

        return Match(
            rodada = rodada,
            dataHora = dataHora.trim(),
            jogo = jogo.trim(),
            link = links.first(),
            timeCasaSigla = timeCasaSigla.trim(),
            escudoTimeCasa = escudoTimeCasa.trim(),
            timeVisitanteSigla = timeVisitanteSigla.trim(),
            escudoTimeVisitante = escudoTimeVisitante.trim(),
            placar = placar?.trim(),
            local = local?.trim()?.replace("Detalhes do jogo", "")
        )

    } catch (e: Exception) {
        return null
    }

}

fun extrairLinks(html: String): List<String> {
    val links = mutableListOf<String>()
    val document = Jsoup.parse(html)
    println(document.body())

    val elements = document.select("a[href*=csrt]")

    for (element in elements) {
        if (element.attr("href").contains("https://www.cbf.com.br/futebol-brasileiro/")) {
            links.add(removerParteUrl(element.attr("href")))
        }
    }

    return links
}

fun removerParteUrl(url: String, parteRemover: String = "https://www.cbf.com.br/futebol-brasileiro/competicoes/"): String {
    return url.replace(parteRemover, "").trim()
}

