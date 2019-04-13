// @GENERATOR:play-routes-compiler
// @SOURCE:/home/magic/mokugo/play-framework-test/conf/routes
// @DATE:Sat Apr 13 17:03:04 EDT 2019


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
