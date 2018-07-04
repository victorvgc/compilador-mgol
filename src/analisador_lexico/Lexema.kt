package analisador_lexico

open class Lexema (estadoFinal: Int, open var lexema: String) {

    lateinit var token: String
    var tipo: String = "indefinido"


    constructor(token1: String, lexema1: String) : this(0, "") {
        token = token1
        lexema = lexema1
    }

    constructor(token1: String, lexema1: String, tipo1: String) : this(0, "") {
        token = token1
        lexema = lexema1
        tipo = tipo1
    }

    init {
        when (estadoFinal) {
            1, 19 -> {
                token = "opm"
                tipo = lexema
            }
            2 ->  {
                token = "num"
                tipo = "int"
            }
            4, 7 -> {
                token = "num"
                tipo = "float"
            }
            9 -> {
                token = "literal"
                tipo = "lit"
            }
            10 -> {
                token = "id"
            }
            14 -> {
                token = "$"
            }
            15, 16, 17 -> {
                token = "opr"
                tipo = lexema
            }
            18 -> {
                token = "rcb"
                tipo = "="
            }
            20 -> {
                token = "AB_P"
            }
            21 -> {
                token = "FC_P"
            }
            22 -> {
                token = "PT_V"
            }
            else -> {
                token = "ERRO"
            }
        }
    }

    override fun toString (): String {
        return "Token: $token | Lexema: $lexema | Tipo: $tipo"
    }

    fun update(lexema: Lexema) {
        if (this.tipo.equals("indefinido"))
            this.tipo = lexema.tipo
    }
}