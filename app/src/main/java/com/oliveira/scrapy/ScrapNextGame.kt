package com.oliveira.scrapy


import org.jsoup.Jsoup

data class Jogo(
    val jogoNumero: String,
    val local: String,
    val data: String,
    val horario: String,
    val transmissao: String,
    val timeCasaNome: String,
    val timeCasaEscudoUrl: String,
    val timeCasaGols: String,
    val timeVisitanteNome: String,
    val timeVisitanteEscudoUrl: String,
    val timeVisitanteGols: String
)

suspend fun getGames(): Pair<List<Jogo>, String> {

    val jogos = mutableListOf<Jogo>()
    var title: String? = null

    val response = connect().table()
    if (response.isSuccessful && response.body() != null) {
        val doc = Jsoup.parse(response.body()!!)
        val metaTag = doc.selectFirst("header.aside-header h3")
        title = metaTag?.text()


        for (link in extrairLinks(doc.body().toString())) {

            println("https://www.cbf.com.br/futebol-brasileiro/$link")

            if (link.isNotEmpty()) {

                val responseJogo = connect().getGames(link)

                if (responseJogo.isSuccessful && responseJogo.body() != null) {
                    val doc2 = Jsoup.parse(responseJogo.body()!!)
                    val jogoElements = doc2.select("div.container div.row div.section-placar-header")
                    for (element2 in jogoElements) {
                        val jogoNumero = element2.selectFirst("span.text-1")?.text()?.trim() ?: ""
                        val local = element2.selectFirst("span.text-2:nth-of-type(1)")?.text()?.trim() ?: ""
                        val data = element2.selectFirst("span.text-2:nth-of-type(2)")?.text()?.trim() ?: ""
                        val horario = element2.selectFirst("span.text-2:nth-of-type(3)")?.text()?.trim() ?: ""
                        val transmissao = element2.selectFirst("span.text-2:nth-of-type(4)")?.text()?.trim() ?: ""

                        val timeCasaNome = element2.selectFirst("h3.time-nome:nth-of-type(1)")?.text()?.trim() ?: ""
                        val timeCasaEscudoUrl = element2.selectFirst("div.time-escudo:nth-of-type(1) img")?.attr("src") ?: ""
                        val timeCasaGols = element2.selectFirst("div.time-gols:nth-of-type(1)")?.text()?.trim() ?: ""

                        val timeVisitanteNome = element2.selectFirst("h3.time-nome:nth-of-type(2)")?.text()?.trim() ?: ""
                        val timeVisitanteEscudoUrl = element2.selectFirst("div.time-escudo:nth-of-type(2) img")?.attr("src") ?: ""
                        val timeVisitanteGols = element2.selectFirst("div.time-gols:nth-of-type(2)")?.text()?.trim() ?: ""

                        val jogo = Jogo(
                            jogoNumero,
                            local,
                            data,
                            horario,
                            transmissao,
                            timeCasaNome,
                            timeCasaEscudoUrl,
                            timeCasaGols,
                            timeVisitanteNome,
                            timeVisitanteEscudoUrl,
                            timeVisitanteGols
                        )

                        jogos.add(jogo)
                    }
                }

            }
        }

        for (jogo in jogos) {
            println(jogo)
        }


    }
    return Pair(jogos, title ?: "")

}

fun extrairLinks(html: String): List<String> {
    val links = mutableListOf<String>()
    val document = Jsoup.parse(html)
    println(document.body())

    val elements = document.select("a[href*=csrt]")

    for (element in elements) {
        if(element.attr("href").contains("https://www.cbf.com.br/futebol-brasileiro/")) {
            links.add(removerParteUrl(element.attr("href")))
        }
    }

    return links
}

fun removerParteUrl(url: String, parteRemover: String = "https://www.cbf.com.br/futebol-brasileiro/competicoes/"): String {
    return url.replace(parteRemover, "").trim()
}

