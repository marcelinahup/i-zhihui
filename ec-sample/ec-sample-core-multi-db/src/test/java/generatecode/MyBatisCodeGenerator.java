package generatecode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class MyBatisCodeGenerator {
	public static void main(String[] args) throws Exception {

		System.out.println("生成代码--开始");

		String path = MyBatisCodeGenerator.class.getResource("").getPath()
				.substring(1);
		String confPath = path + "MBG_configuration.xml";

		System.out.println(path);
		File configFile = new File(confPath);

		List<String> warnings = new ArrayList<String>();
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		boolean overwrite = true;
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
				callback, warnings);
		myBatisGenerator.generate(null);
		System.out.println("生成代码--完成");
	}
}
