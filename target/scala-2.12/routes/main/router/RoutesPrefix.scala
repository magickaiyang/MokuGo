// @GENERATOR:play-routes-compiler
// @SOURCE:/home/magic/mokugo/conf/routes
// @DATE:Sat Apr 13 17:32:25 EDT 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
