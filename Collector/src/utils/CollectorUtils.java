package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import objs.TableObject;

public class CollectorUtils {

	public static void saveToFile(TableObject table, String filePrefix) {
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String stamp = time.format(new java.util.Date());
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePrefix + "-" + stamp + ".csv"));
			for (ArrayList<String> rowData : table.getData()) {
					for (String s : rowData) {
						bw.write("\"" + s + "\"" + ",");
					}
					bw.write("\n");
				}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
	}
	
	public static String chineseToPinyin(String chinese) throws BadHanyuPinyinOutputFormatCombination {
		String pinyin = "";
		char[] chineseChars = chinese.toCharArray();
		HanyuPinyinOutputFormat  format = new HanyuPinyinOutputFormat ();
		
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		
		for (int i=0; i<chineseChars.length; ++i) {
			if (chineseChars[i] > 128) {
				pinyin += PinyinHelper.toHanyuPinyinStringArray(chineseChars[i], format)[0];
			}
		}
		
		return pinyin;
	}
	
	public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
		File dir = new File("E:\\项目\\三级数据网\\data");
		MyFilter filter = new MyFilter(".mht");
		System.out.println(dir.listFiles().length);	
		File[] fls = dir.listFiles(filter);

		for (File f : fls) {
			System.out.println(chineseToPinyin(f.getName()));
		}
	}

	static class MyFilter implements FilenameFilter {
		String type;
		
		public MyFilter(String type) {
			this.type = type;
		}
		
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(type);
		}
		
	}
}
