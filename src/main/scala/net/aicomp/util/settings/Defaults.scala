package net.aicomp.util.settings

/**
 * Object containing default values for the application.
 */
object Defaults {

  /**
   * The default XML parser for settings.
   */
  val XML_SETTINGS_PARSER_CLASS_NAME = "net.javachallenge.util.settings.XMLSettingsParser"

  /**
   * The default file format for settings.
   */
  val SETTINGS_FORMAT = "xml"

  /**
   * The default locale for the application if not present in configuration file.
   */
  val LOCALE = "ja"

  /**
   * The default fallback for the application if not present in configuration file.
   */
  val FALLBACK = "ja"

  /**
   * The default character of new line.
   */
  val NEW_LINE = System.getProperty("line.separator")
}