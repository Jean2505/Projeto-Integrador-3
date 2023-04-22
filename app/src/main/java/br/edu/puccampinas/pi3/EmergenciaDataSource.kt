package br.edu.puccampinas.pi3

fun emergenciaList(): List<Emergencia>{
    return listOf(
        Emergencia(nome = "João Augusto Ferraz",
            telefone = "11111-1111",
            foto1 = "cywhrvbuyewb",
            foto2 = "cbhewvcbuye",
            foto3 = "cbhuredbcuj",
            status = "Nova",
            dataHora = "22/04/2023 15:27"),

        Emergencia(nome = "Jean Victor Chaves",
            telefone = "22222-2222",
            foto1 = "cywhrvbuyewb",
            foto2 = "cbhewvcbuye",
            foto3 = "cbhuredbcuj",
            status = "Nova",
            dataHora = "25/05/2023 05:25"),

        Emergencia(nome = "Guilherme Vieira",
            telefone = "33333-3333",
            foto1 = "cywhrvbuyewb",
            foto2 = "cbhewvcbuye",
            foto3 = "cbhuredbcuj",
            status = "Nova",
            dataHora = "26/05/2023 15:27")
    )
}