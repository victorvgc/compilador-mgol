package analisador_semantico

class ErroSemantico (val erro : String) {

    override fun toString(): String {
        return "Erro Semântico: $erro."
    }
}