package playmeta
package gen

import ast.CreateTable

trait Generator {
  def apply(ct: CreateTable, pkg: Option[String] = None): String
  def name(ct: CreateTable): String
  val subpackage: String
}
