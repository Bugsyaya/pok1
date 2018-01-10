package controllers

import play.api._
import play.api.mvc._

class ApplicationController extends Controller {
	
	def getName = Action {
		Ok("Jim")
	}
	
}