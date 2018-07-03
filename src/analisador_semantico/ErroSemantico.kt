package analisador_semantico

class ErroSemantico (val erro : String, val linhaErro: Int) {

    override fun toString(): String {
        return "Erro Semântico: $erro. Linha: $linhaErro"
    }
}