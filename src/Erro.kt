class Erro(override var lexema: String, linha: Int, coluna: Int): Lexema("ERRO", lexema) {
    val descricao = "Erro na linha: $linha, coluna: $coluna. Símbolo não esperado."

    override fun toString(): String {
        return "Token: $token | Lexema: $lexema | Descricao: $descricao"
    }
}