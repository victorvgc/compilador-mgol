open class Lexema (estadoFinal: Int, open var lexema: String) {

    var token: String

    constructor(token1: String, lexema1: String) : this(0, "") {
        token = token1
        lexema = lexema1
    }
    init {
        when (estadoFinal) {
            1 -> token = "OPM"
            2 -> token = "Num"
            4 -> token = "Num"
            7 -> token = "Num"
            9 -> token = "Literal"
            10 -> token = "id"
            14 -> token = "EOF"
            15 -> token = "OPR"
            16 -> token = "OPR"
            17 -> token = "OPR"
            18 -> token = "RCB"
            19 -> token = "OPM"
            20 -> token = "AB_P"
            21 -> token = "FC_P"
            22 -> token = "PT_V"
            else -> token = "ERRO"
        }
    }

    var tipo: String = "indefinido"

    override fun toString (): String {
        return "Token: " + token + " | Lexema: " + lexema + " | Tipo: " + tipo
    }

    fun update(lexema: Lexema) {
        if (this.tipo.equals("indefinido"))
            this.tipo = lexema.tipo
    }
}