package com.oliveira.scrapy


import org.jsoup.Jsoup
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface APICbf {
    @GET("competicoes/campeonato-brasileiro-serie-a/{year}")
    suspend fun table(@Path("year") year: String ): Response<String>

    @GET("competicoes/{path}")
    suspend fun getGames(@Path("path") path: String): Response<String>

}


fun connect(url:String = "https://www.cbf.com.br/futebol-brasileiro/"): APICbf = Retrofit.Builder()
    .baseUrl(url)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()
    .create(APICbf::class.java)

data class DadosTime(
    val img: String,
    val nome: String,
    val posicao: String,
    val pontos: String,
    val jogos: String,
    val vitorias: String,
    val empates: String,
    val derrotas: String,
    val golsFeitos: String,
    val golsSofridos: String,
    val saldoGols: String,
    val cartoesAmarelos: String,
    val cartoesVermelhos: String,
    val aproveitamento: String,
    val ultimosResultados: String
)


suspend fun getAllPosition(year: String): Pair<List<DadosTime>, String> {

    val listaDadosTimes = mutableListOf<DadosTime>()
    var title: String? = null

    val response = connect().table(year)
    if (response.isSuccessful && response.body() != null) {
        val htmlDoc = Jsoup.parse(response.body()!!)
        val metaTag = htmlDoc.select("h2.title.uppercase")
        title = metaTag.text()
        print(title)

        val rows = htmlDoc.select("tr.expand-trigger")

        for (row in rows) {
            val imgTag = row.selectFirst("img")
            val pathImg = imgTag?.attr("src")

            val cells = row.select("td")
            val posicao = cells[0].select("b").text()
            val nome = cells[0].select("span.hidden-xs").text()
            val pontos = row.selectFirst("th[scope=row]")?.text()
            val jogos = cells[1].text()
            val vitorias = cells[2].text()
            val empates = cells[3].text()
            val derrotas = cells[4].text()
            val golsFeitos = cells[5].text()
            val golsSofridos = cells[6].text()
            val saldoGols = cells[7].text()
            val cartoesAmarelos = cells[8].text()
            val cartoesVermelhos = cells[9].text()
            val aproveitamento = cells[10].text()
            val ultimosResultados = cells[11].select("span.badge").joinToString(" ") { it.text() }
            val dadosTime = DadosTime(
                pathImg ?: "",
                nome,
                posicao,
                pontos ?: "-",
                jogos,
                vitorias,
                empates,
                derrotas,
                golsFeitos,
                golsSofridos,
                saldoGols,
                cartoesAmarelos,
                cartoesVermelhos,
                aproveitamento,
                ultimosResultados
            )
            listaDadosTimes.add(dadosTime)
        }

//        listaDadosTimes.forEach { println(it) }
    }
    return Pair(listaDadosTimes, title ?: "")

}
