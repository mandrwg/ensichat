package com.nutomic.ensichat.protocol.header

import java.util.{Date, GregorianCalendar}

import android.test.AndroidTestCase
import com.nutomic.ensichat.protocol.body.{PaymentInformation, Text}
import com.nutomic.ensichat.protocol.{Address, AddressTest}
import junit.framework.Assert._

object ContentHeaderTest {

  val h1 = new ContentHeader(AddressTest.a1, AddressTest.a2, 1234,
    Text.Type, 123, new GregorianCalendar(1970, 1, 1).getTime, false, 5)

  val h2 = new ContentHeader(AddressTest.a1, AddressTest.a3,
    30000, Text.Type, 8765, new GregorianCalendar(2014, 6, 10).getTime, false, 20)

  val h3 = new ContentHeader(AddressTest.a4, AddressTest.a2,
    250, Text.Type, 77, new GregorianCalendar(2020, 11, 11).getTime, false, 123)

  val h4 = new ContentHeader(Address.Null, Address.Broadcast,
    ContentHeader.SeqNumRange.last, 0, 0xffff, new Date(0L), false, 0xff)

  val h5 = new ContentHeader(Address.Broadcast, Address.Null,
    0, 0xff, 0, new Date(0xffffffffL), false, 0)

  val h6 = new ContentHeader(AddressTest.a1, AddressTest.a2, 1234,
    PaymentInformation.Type, 123, new GregorianCalendar(2015, 8, 9).getTime, false, 5)

  val headers = Set(h1, h2, h3, h4, h5, h6)

}

class ContentHeaderTest extends AndroidTestCase {

  def testSerialize(): Unit = {
    ContentHeaderTest.headers.foreach{h =>
      val bytes = h.write(0)
      assertEquals(bytes.length, h.length)
      val (mh, length) = MessageHeader.read(bytes)
      val chBytes = bytes.drop(mh.length)
      val (header, remaining) = ContentHeader.read(mh, chBytes)
      assertEquals(h, header)
      assertEquals(0, remaining.length)
    }
  }

}
