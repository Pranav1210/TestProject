package com.cisco.ccc.cats

import cats.FlatMap
import cats.implicits._
import cats.data.Reader

final case class Kleisli[F[_], A, B](run: A ⇒ F[B]) {
  def compose[Z](k: Kleisli[F, Z, A])(implicit F: FlatMap[F]): Kleisli[F, Z, B] =
    Kleisli[F, Z, B](z ⇒ k.run(z).flatMap(run))
}

object Runtime {
  case class Course(desc: String, code: String)

  class AuthService {
    def isAuthorised(userName: String): Boolean = userName.startsWith("J")
  }

  class CourseService {
    def register(course:       Course,
                 isAuthorised: Boolean,
                 name:         String) = {
      if (isAuthorised)
        s"User $name registered for the course: ${course.code}"
      else
        s"User: $name is not authorised to register for course: ${course.code}"
    }
  }

  case class CourseManager(course:        Course,
                           userName:      String,
                           authService:   AuthService,
                           courseService: CourseService)

  def isAuthorized = Reader[CourseManager, Boolean] { courseMgr =>
    courseMgr.authService.isAuthorised(courseMgr.userName)
  }

  def register(isAuthorised: Boolean) = Reader[CourseManager, String] { courseMgr =>
    courseMgr.courseService.register(courseMgr.course, isAuthorised, courseMgr.userName)
  }

  val registrationRecipe = for {
    auth <- isAuthorized
    registered <- register(auth)
  } yield registered

  def main(args: Array[String]): Unit = {
    val course = Course("Computer Science", "CS01")
    val result = registrationRecipe.run(CourseManager(course, "Don", new AuthService, new CourseService))
    println(result)
  }
}