/**
  * Whenever anything is a bit anoying it might be tested out here. The changing content of this file is
  * not commited after the initial commit, but if you're looking at this code you might need it.
  */
val printTest="I am a string"
def testMethod(): String=>String = {
  printTest => "This is a string "+printTest

}
val testMethodTwo = testMethod

testMethodTwo("THE STRING IS IN")
