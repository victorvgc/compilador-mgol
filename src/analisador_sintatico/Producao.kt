package analisador_sintatico

class Producao (val reducao: String, val argumentos: List<String>) {
    override fun toString(): String {
        return "Produção: $reducao -> ${argumentos.joinToString(" ")}"
    }
}