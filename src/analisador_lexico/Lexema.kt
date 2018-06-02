package analisador_lexico

open class Lexema (estadoFinal: Int, open var lexema: String) {

    var token: String

    constructor(token1: String, lexema1: String) : this(0, "") {
        token = token1
        lexema = lexema1
    }
    init {
        token = when (estadoFinal) {
            1, 19 -> "opm"
            2, 4, 7 -> "num"
            9 -> "literal"
            10 -> "id"
            14 -> "eof"
            15, 16, 17 -> "opr"
            18 -> "rcb"
            20 -> "AB_P"
            21 -> "FC_P"
            22 -> "PT_V"
            else -> "ERRO"
        }
    }

    var tipo: String = "indefinido"

    override fun toString (): String {
        return "Token: $token | Lexema: $lexema | Tipo: $tipo"
    }

    fun update(lexema: Lexema) {
        if (this.tipo.equals("indefinido"))
            this.tipo = lexema.tipo
    }
}