package controllers

import play.api.mvc.{Action, Controller}

class CorsController  extends Controller {
	
	def accept(path: String) = Action {
		Ok("").withHeaders(ACCESS_CONTROL_ALLOW_HEADERS -> "*",
			ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true",
			ACCESS_CONTROL_ALLOW_METHODS -> "POST, GET, DELETE, UPDATE, PATCH, PUT, OPTIONS",
			ACCESS_CONTROL_ALLOW_ORIGIN -> "*"
		)
	}
	
}
