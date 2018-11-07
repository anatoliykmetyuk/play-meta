package playmeta.gen

object util {
  implicit class IndentString(str: String) {
    def indent(width: Int): String = {
      val split: List[String] = str.split("\n").toList
      (split.head :: split.tail.map(line => " " * width + line))
        .mkString("\n")
    }
  }
}