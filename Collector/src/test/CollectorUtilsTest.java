package test;

import static org.junit.Assert.*;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.junit.Test;

import utils.CollectorUtils;

public class CollectorUtilsTest {

	@Test
	public void testChineseToPinyin() {
		try {
			assertEquals(CollectorUtils.chineseToPinyin("ÓÃ»§Ãû³Æ"), "YONGHUMINGCHENG");
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
