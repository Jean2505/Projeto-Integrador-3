package br.edu.puccampinas.pi3

fun emergenciaList(): List<Emergencia>{
    return mutableListOf(
        Emergencia(nome = "João Augusto Ferraz",
            telefone = "11111-1111",
            foto1 = "",
            foto2 = "",
            foto3 = "",
            status = "Nova",
            dataHora = System.currentTimeMillis().toString(),
            emergencia = "a"),

        Emergencia(nome = "Jean Victor Chaves",
            telefone = "22222-2222",
            foto1 = "",
            foto2 = "",
            foto3 = "",
            status = "Nova",
            dataHora = "25/05/2023 05:25",
            emergencia = "a"),

        Emergencia(nome = "Guilherme Vieira",
            telefone = "33333-3333",
            foto1 = "",
            foto2 = "",
            foto3 = "",
            status = "Nova",
            dataHora = "26/05/2023 15:27",
            emergencia = "a")
    )
}