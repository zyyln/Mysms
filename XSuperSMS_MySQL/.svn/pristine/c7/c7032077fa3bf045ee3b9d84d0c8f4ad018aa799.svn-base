
XSuperSMS V3.1 project
	create:zyy
	createDate:2015-07-31
	update:zyy
	updateDate:2016-12-30
	主干：
	a.主干版本是稳定的版本
	b.开发的时候全部在分支上开发
	c.开发完毕后合并到主干上发布
	
	@打包程序注意事项:{
	1、SmsConfig.isDebug = false;
	2、manifest文件中application添加android:debuggable="false"
	3、目前真实版本的入口ServerApi文件中mDirectory = "XSuperMES2016"
	4、真实版与测试版的端口号都应确定后在com.xuesi.sms. ServerApi中进行修改
	5、每次打包外发都需要把版本号增加
	...
	}
	
	@打包用到的文件:{
	//1-2
	1、keyStore：keyStore_云端SMS
	2、说明文件:keyStore_云端SMS.txt
	//3-4为了能够直接覆盖之前ORCALE版本的SMS
	3、发布在阿里云上的签名文件是：keyStore_XSuperSMS
	4、说明文件:keyStore_XSuperSMS.txt
	}
	
	@更新文件：{
	1、UpdateInfo.xml，保存新版本的信息
	2、云端SMS_v1.0.0.1.apk，新版本apk
	3、放在服务器Update文件夹下
	}
	
	@libs.armeabi:{
	barcodereader44.so	
	--KT45条形码扫描所用的链接库
	
	IAL.so	
	--TT35\KT45条形码扫描所用的链接库
	
	SDL.so	
	--TT35\KT45条形码扫描所用的链接库
	
	package.so 
	--KT45\TT35超高频扫描所用的链接库
	
	uhfrfid.so 
	--KT45\TT35超高频扫描所用的链接库
	
	achartengine-1.1.0.jar
	-- 图表引擎
	-- 作业看板图表绘制
	
	android_extension.jar
	-- UROVO设备所用的条形码扫描包
	DataCollection.jar
	-- HoneyWell(霍尼韦尔)设备所用的条形码扫描包
	}
	
	@strings.xml内容优化方案：{
	1、按物品类别分类，而不是按画面，如：涉及钢板的字符，涉及垛位的字符 ，目的更好管理，避免重复字符
	2、key值命名最好和实体类字段名一致
	3、库房和仓库，库位和垛位统一
	}