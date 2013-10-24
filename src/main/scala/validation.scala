package net.rosien.scalaz

object ValidationExamples {
  import scalaz._
  import Scalaz._

  case class Version(
      major: Int, // >= 0
      minor: Int) // >= 0

  object Version {
    def validDigit(digit: Int): Validation[String, Int] =
      (digit >= 0) ? digit.success[String] | "digit must be >= 0".fail

    def validate(major: Int, minor: Int): ValidationNel[String, Version] =
      (validDigit(major).toValidationNel |@| 
        validDigit(minor).toValidationNel) {
        Version(_, _)
      }
  }

  val major: Int = 1
  val minor: Int = 1
  val version = Version.validate(major, minor)

  version | Version(1, 0) // provide default

  // handle failure and success
  version.fold(
    (fail: NonEmptyList[String]) => (),
    (success: Version) => ())

  val maj = Version.validDigit(major).toValidationNel
  val min = Version.validDigit(minor).toValidationNel
  // Both ValidationNel[String, Int]
  //                            ^^^

  val mkVersion = Version(_, _)
  // (Int, Int) => Version

  val version2 = (maj |@| min) { mkVersion }
  // ValidationNel[String, Version]
  //                       ^^^^^^^

  case class Dependency(
      organization: String, // non-empty && has namespace
      artifactId: String,   // non-empty
      version: Version)     // validated

  object Dependency {
    def nonEmptyValue(name: String, value: String) = value.isEmpty ?
        "%s cannot be empty".format(name).fail[String] | value.success

    def validOrganizationNamespace(org: String) = org.contains(".") ?
        org.success[String] | "organization must be some kind of namespace".fail

    def validOrganization(org: String) =
      (nonEmptyValue("organization", org).toValidationNel |@| validOrganizationNamespace(org).toValidationNel) {
      (_, _) => org
    }

    def validArtifactId(id: String) = nonEmptyValue("artifactId", id)

    def validate(organization: String, artifactId: String, version: ValidationNel[String, Version]) = {
      (validOrganization(organization) |@| validArtifactId(artifactId).toValidationNel |@| version) {
        Dependency(_, _, _)
      }
    }
  }
}