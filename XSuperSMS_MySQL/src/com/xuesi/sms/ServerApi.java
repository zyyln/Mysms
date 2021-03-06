package com.xuesi.sms;

import com.xuesi.sms.bean.BaseModel;
import com.xuesi.sms.bean.BaseVo;
import com.xuesi.sms.bean.CraneByType;
import com.xuesi.sms.bean.CraneWorkDetail;
import com.xuesi.sms.bean.GetBillNoAndSheetMsg;
import com.xuesi.sms.bean.LoginJudge;
import com.xuesi.sms.bean.ResultCode;
import com.xuesi.sms.bean.Sheet;
import com.xuesi.sms.bean.Stack;
import com.xuesi.sms.bean.StackExplain;
import com.xuesi.sms.bean.StoreHouse;
import com.xuesi.sms.util.CalendarUtil;
import com.xuesi.sms.util.SPHelper;

/**
 * 服务器接口汇总<br>
 * 接口后期优化，和服务器端协作：<br>
 * 1、筛选垛位和钢板的接口不用返回两个list，可以加一个标志位，区分是符合条件的数据和还是不符合<br>
 * 2、筛选pad.cfg文件的接口，整合到其他cfg文件中<br>
 * 3、命名方式改为cfg文件名加接口名称(可简写)如 ：USER_LOGIN<br>
 * 
 * @author XS-PC014
 * 
 */
public class ServerApi {

	/** ServerApi实例对象 */
	private static ServerApi instance;

	private ServerApi() {
	}

	/** 单例模式中获取唯一的ServerApi实例 */
	public static ServerApi getInstance() {
		if (null == instance) {
			instance = new ServerApi();
		}
		return instance;
	}

	/** 重置空指针 */
	public static void setNull() {
		instance = null;
	}

	/** cookie通行证 */
	public static final String COOKIE_PASSPORT = "passport";
	public static String passport;

	/** cookie权限GID */
	public static final String COOKIE_MILLCODE = "millCode";
	public static String millCode;

	/** 接口传入参数：分厂号 */
	public static final String PARA_FACTORYCODE = "factoryCode";
	public static String factoryCode;

	/** 存储当前登录用户 */
	public static String account;
	/** 存储当前登录用户所属公司 */
	public static String comp;
	/** 存储当前服务器日期 */
	public static String date;

	// -- HTTP ---------
	/** 入口协议 */
	private static final String mPortalProtocol = "http://";
	/** 入口地址njxstzt.sms.net 121.40.104.243 taoliao.dfstw.com */
	public static final String mPortalAddress = "121.40.104.243";

	/** 入口端口 */
	public static String mPortalPort = "8089";
	// /** 入口目录 */
	// private static final String mDirectory = "XSuperMES";
	// 真实环境下的入口
	private static final String mDirectory = "XSuperMES2016";
	/** 更新入口目录 */
	private static final String mDirectoryUp = "Update";
	/** 服务超时 */
	public static int mTimeout = 8 * 1000;

	/** 获取广告URI */
	public static final String imageUri = "http://www.xsupercutting.com/inc/css/ad/adbg.jpg?v="
			+ CalendarUtil.getTodayDateL();

	public static final String advertUri = "http://www.xsupercutting.com/help/des.htm";
	/** PAD帮助手册地址 **/
	public static final String HelpUri = "http://xsupercutting.com:8089/Help/index.html#";
	/** 配置列表字段 */
	public static final String CONFIGURE_FIELDS = "http://xsupercutting.com/padmin/padtopset.htm";
	/** 注册链接 */
	public static final String REGISTER_URL = "http://xsupersms.mikecrm.com/f.php?t=uc2dTg";
	public static final String CONFIGURE_SHEET = "pad/topset/sheet";
	public static final String CONFIGURE_BILLS = "padmin/Report/Sheetorder";
	public static final String LANG = "ZH-CN";

	/**
	 * url== http://192.168.1.232:8088/Update/UpdateInfo.xml
	 * 
	 * @return
	 */
	public String getUpdateUrl() {
		if (SmsConfig.isDebug) {
			String ip = SPHelper.getInstance().getStringFromSp(SPHelper.KEY_IP);
			String port = null;
			if (SmsConfig.isTest) {
				// 体验版端口
				port = "8091";
			} else {
				port = SPHelper.getInstance()
						.getStringFromSp(SPHelper.KEY_PORT);
			}
			if (ip.length() > 0 && port.length() > 0) {
				return mPortalProtocol + ip + ":" + port + "/" + mDirectoryUp;
			} else {
				return mPortalProtocol + mPortalAddress + ":" + mPortalPort
						+ "/" + mDirectoryUp;
			}
		} else {
			if (SmsConfig.isTest) {
				// 体验版端口
				mPortalPort = "8091";
			}
			return mPortalProtocol + mPortalAddress + ":" + mPortalPort + "/"
					+ mDirectoryUp;
		}
	}

	// 打印接口个数
	// int i = 0;
	// Log.w(ServerApi.class.getName(), "ip== " + ip + "    " + i++);
	/**
	 * 
	 * @return
	 */
	public String getUrl() {
		if (SmsConfig.isDebug) {
			String ip = SPHelper.getInstance().getStringFromSp(SPHelper.KEY_IP);
			String port = null;
			if (SmsConfig.isTest) {
				// 体验版端口
				port = "8091";
			} else {
				port = SPHelper.getInstance()
						.getStringFromSp(SPHelper.KEY_PORT);
			}
			if (ip.length() > 0 && port.length() > 0) {
				return mPortalProtocol + ip + ":" + port + "/" + mDirectory;
			} else {
				return mPortalProtocol + mPortalAddress + ":" + mPortalPort
						+ "/" + mDirectory;
			}
		} else {
			if (SmsConfig.isTest) {
				// 体验版端口
				mPortalPort = "8091";
			}
			return mPortalProtocol + mPortalAddress + ":" + mPortalPort + "/"
					+ mDirectory;
		}
	}

	// ------------------------------------------
	// ----------------user.cfg------------------
	// ------------------------------------------
	/**
	 * 功能: 用户登录<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：<br>
	 * 备注：<br>
	 * */
	public String USER_LOGIN = getUrl() + "/api/user/login";
	/**
	 * 功能: 修改密码的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link BaseModel}<br>
	 * 画面：<br>
	 * 备注：不需要factoryCode<br>
	 * */
	public String USER_CHANGE_PSD = getUrl() + "/api/user/changePassword";

	// ------------------------------------------------
	// -----------------Workboard.cfg------------------
	// ------------------------------------------------
	/**
	 * 功能: 获取作业面板中数据整合后的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link }<br>
	 * 画面：作业看板<br>
	 * 备注：<br>
	 * */
	public String API_WB_DATA_TOTAL = getUrl()
			+ "/api/WorkBoard/getWbDataTotal";
	/**
	 * 功能: 获取计划中的已完工数量, 二报数量和接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link }<br>
	 * 画面：计划进展<br>
	 * 备注：<br>
	 * */
	// public String API_PLAN_LIST_FINISH = getUrl() +
	// "/api/WorkBoard/getPlanListFinishQty";
	/**
	 * 功能: 获取计划中的总数量, 计划数量和的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link }<br>
	 * 画面：计划进展<br>
	 * 备注：<br>
	 * */
	// public String API_PLAN_LIST_PLAN = getUrl()
	// + "/api/WorkBoard/getPlanListPlanQty";
	/**
	 * 功能: 获取Planlist中的计划进展详细 (未加工，加工中的数量)<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link }<br>
	 * 画面：计划进展<br>
	 * 备注：<br>
	 * */
	// public String API_PLAN_LIST_NUMNESTED = getUrl()
	// + "/api/WorkBoard/getPlanListNumNested";
	/**
	 * 功能: 获取计划里表中逾期零件数的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link }<br>
	 * 画面：计划进展<br>
	 * 备注：<br>
	 * */
	// public String API_PLAN_LIST_OVER_DUE = getUrl()
	// + "/api/WorkBoard/getPlanListOverDueQty";
	/**
	 * 功能: 获取产品完成情况(名称, 已完工和未完工数量)的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link }<br>
	 * 画面：计划进展<br>
	 * 备注：<br>
	 * */
	// public String API_ASSEMBLY_N = getUrl() +
	// "/api/WorkBoard/getAssemblyNQty";
	/**
	 * 功能: 获取单位时间内切割钢板张数和完工零件重量的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：产量<br>
	 * 备注：<br>
	 * */
	// public String API_YIELD_INFO = getUrl() + "/api/WorkBoard/getYieldInfo";
	/**
	 * 功能: 获取单位内钢板总/新增张数和重量的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：库存<br>
	 * 备注：<br>
	 * */
	public String API_SHEET_INFO = getUrl() + "/api/WorkBoard/getSheetInfo";
	/**
	 * 功能: 获取单位时间内余料总/新增张数和重量的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：<br>
	 * 备注：<br>
	 * */
	public String API_SHEET_RCK_INFO = getUrl()
			+ "/api/WorkBoard/getSheetRckInfo";
	/**
	 * 功能: 获取单位时间内在制品总/新增数和重量的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：<br>
	 * 备注：<br>
	 * */
	// public String API_SHOP_PART_INFO = getUrl()
	// + "/api/WorkBoard/getShopPartInfo";
	/**
	 * 功能: 获取利用率的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：利用率<br>
	 * 备注：<br>
	 * */
	// public String API_USED_TOTAL_RATE = getUrl() +
	// "/api/WorkBoard/getUsedRate";

	// -----------------------------------------------
	// ----------------SegmentConfig.cfg----------------------
	// -----------------------------------------------
	/**
	 * 功能: 获取导入EXCEL的字段设定的信息<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：条码绑定Z<br>
	 * 备注：<br>
	 * */
	public String SEGMENTCONFIG_GETFILEDS = getUrl()
			+ "/api/SegmentConfig/getSegmentInfo";
	// -----------------------------------------------
	// ----------------Order.cfg----------------------
	// -----------------------------------------------
	/**
	 * 功能: 获取采购单号<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：条码绑定,入库<br>
	 * 备注：<br>
	 * */
	public String ORDER_GETBILLNOS = getUrl() + "/api/order/GetBillNos";
	/**
	 * 功能: 根据单号获取钢板<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link Sheet}<br>
	 * 画面：条码绑定,入库<br>
	 * 备注：<br>
	 * */
	public String ORDER_GETORDERLIST = getUrl() + "/api/order/GetOrderList";
	/**
	 * 功能: 修改条形码<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link BaseModel}<br>
	 * 画面：钢板明细<br>
	 * 备注：<br>
	 * */
	public String ORDER_UPDATEBARCODE = getUrl()
			+ "/api/order/updataBarCodeBySheetId";

	// -----------------------------------------
	// ----------Sheet.cfg----------------------
	// -----------------------------------------
	/**
	 * 功能: 钢板入库更新(一级库FSK)<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：入库<br>
	 * 备注：批量
	 * */
	public String SHEET_INSTACK = getUrl() + "/api/sheet/INStack";
	/**
	 * 功能: 钢板入库更新(余料库RCK)<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：<br>
	 * 备注：已废，改用API_ADDREMNANTSTACK<br>
	 * public String API_INREMSTACK = getUrl() + "/api/sheet/INRemStack";
	 * */
	/**
	 * 功能: 钢板倒垛更新<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：倒垛<br>
	 * 备注：<br>
	 * 批量
	 * */
	public String SHEET_MOVESTACK = getUrl() + "/api/sheet/MoveStack";
	/**
	 * 功能: 查询所有钢板是否有在盘点状态<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link BaseVo}<br>
	 * 画面：<br>
	 * 备注：<br>
	 * */
	public String SHEET_GETISLOCK = getUrl() + "/api/sheet/GetIsLock";
	/**
	 * 功能: 根据库房类型(FSK,SCK,RCK)获取库房信息<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：<br>
	 * 备注：<br>
	 * */
	public String SHEET_GETHOUSEID = getUrl() + "/api/sheet/GetHouseId";
	/**
	 * 功能: 根据货位编号或者条形码获取钢板信息<br>
	 * 传入值: <br>
	 * 返回值: 返回已入库钢板<br>
	 * 解析体:{@link Sheet}<br>
	 * 画面：盘点明细，条码扫描，钢板明细，倒垛<br>
	 * 备注：分页接口,提检号,【ZYY-20150831-添加一个锈蚀返回字段】<br>
	 */
	public String SHEET_GETSHEETLIST = getUrl()
			+ "/api/sheet/GetSheetListByStackNo";

	// --------------------------------------------------
	// -----------------Remnant.cfg----------------------
	// --------------------------------------------------
	/**
	 * 功能: 钢板入库更新(余料库RCK)<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：<br>
	 * 备注：和PC共用<br>
	 * 批量
	 * */
	public String API_ADDREMNANTSTACK = getUrl()
			+ "/api/Remnant/addRemnantStack";

	// -------------------------------------------
	// ------------------Grant.cfg----------------
	// -------------------------------------------
	/**
	 * 功能: 获取发料单号<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：出库<br>
	 * 备注：<br>
	 * */
	public String API_GETGRANTID = getUrl() + "/api/grant/getGrantId";
	/**
	 * 功能: 根据选择的单号获取单号详细<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：出库<br>
	 * 备注：<br>
	 * */
	public String API_GETGRANT_DETAILINFO = getUrl()
			+ "/api/grant/getGrantDetailInfo";
	/**
	 * 功能: 出库更新 <br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：出库<br>
	 * 备注：pad&pc<br>
	 * 批量
	 * */
	public String API_FINISHSEND = getUrl() + "/api/Grant/finishSend";

	// --------------------------------------------
	// ---------------SwapProjectId.cfg------------
	// --------------------------------------------
	/**
	 * 功能: 获取钢板信息<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：出库<br>
	 * 备注：<br>
	 * */
	public String API_GETSHEETIN_STACKINFO = getUrl()
			+ "/api/SwapProjectId/getSheetInstackInfo";
	/**
	 * 功能: 选定出库<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：出库<br>
	 * 备注：pad&pc<br>
	 * 批量
	 * */
	public String API_UPDATEOUTSTCKSELECTED = getUrl()
			+ "/api/SwapProjectId/updateOutStckSelected";

	// ---------------------------------------------
	// -------------StoreHouseManage.cfg------------
	// ---------------------------------------------
	/**
	 * 功能: 开始盘点(插入库存盘点表)<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link BaseModel}<br>
	 * 画面：钢板库盘点<br>
	 * 备注：空库房可以盘点,和PC共用<br>
	 * */
	public String API_ADDSHEETDETAILPD = getUrl()
			+ "/api/StoreHouseManage/addSheetDetailPD";

	// ---------------------------------------------
	// -------------StoreStackmng.cfg---------------
	// ---------------------------------------------
	/**
	 * 功能: 根据库房编号获取所有货位-也可用于货位推荐<br>
	 * 查询货位的当前数量和当前高度<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link Stack}<br>
	 * 画面：<br>
	 * 备注：<br>
	 * */
	public String API_GET_STACKINFO = getUrl()
			+ "/api/StoreStackMng/getStackInfo";
	// /**
	// * 功能: 根据货位ID获取货位定义信息<br>
	// * 传入值: <br>
	// * 返回值: <br>
	// * 解析体:{@link StackExplain}<br>
	// * 画面：库位总览、钢板明细<br>
	// * 备注：<br>
	// */
	// public String API_GET_STACKINFO_BYID = getUrl()
	// + "/api/StoreStackMng/getStackInfoById";
	/**
	 * 功能: 根据货位ID获取货位自定义信息<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link StackExplain}<br>
	 * 画面：<br>
	 * 备注：<br>
	 */
	public String API_GET_STACKSETMSG = getUrl() + "/api/pad/getStackSetMsg";

	// ----------------------------------------------
	// -------------common.cfg-----------------------
	// ----------------------------------------------
	/**
	 * 功能: 登陆之后初始化数据<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link BaseModel}<br>
	 * 画面：<br>
	 * 备注：命名方式改为cfg名称加接口名称(可简写)<br>
	 * */
	public String common_initialize = getUrl() + "/api/common/testInitialize";

	public String API_INSERTEXPERIENCE = getUrl()
			+ "/api/common/InsertExperience";

	// -------------------------------------------------------------------
	// ----Pad.cfg(部分接口与其他cfg接口功能重复，可优化，后期删除pad.cfg)-----------
	// -------------------------------------------------------------------
	/**
	 * 功能: 获取服务器时间的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：作业看板<br>
	 * 备注：<br>
	 * */
	public String API_GETSYSDATE = getUrl() + "/api/pad/getSysdate";
	/**
	 * 功能: 根据输入内容模糊查询钢板属性的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link GetBillNoAndSheetMsg}<br>
	 * 画面：条码绑定<br>
	 * 备注：<br>
	 * */
	public String API_GETSHEETMSG = getUrl() + "/api/pad/getMatchInfo";
	/**
	 * 功能: 根据单号确认绑定<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link ResultCode}<br>
	 * 画面：<br>
	 * 备注：批量
	 * */
	public String API_UPDATABARCODEBYSHEETID = getUrl()
			+ "/api/pad/updataBarCodeBySheetId";
	/**
	 * 功能: 填写信息确认绑定<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：<br>
	 * 备注：批量
	 * */
	public String API_INSERTSHEETANDREMNANT = getUrl()
			+ "/api/pad/insertSheetAndRemnant";
	/**
	 * 功能: 发布采购单号<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：<br>
	 * 备注：批量
	 * */
	public String API_RELEASEBILLNO = getUrl()
			+ "/api/pad/UpdartOrderByStatusPAD";
	/**
	 * 功能: 根据条形码获取钢板数据(未入库钢板)<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：入库<br>
	 * 备注：<br>
	 * */
	public String API_GETBARCODE = getUrl() + "/api/pad/getBarCode";
	/**
	 * 功能: 获取切割生成的余料<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：入库<br>
	 * 备注：<br>
	 * */
	public String API_GETREMLIST = getUrl() + "/api/pad/GetPADRemnantList";
	/**
	 * 功能: 钢板二级库SCK入库的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：<br>
	 * 备注：功能暂时去除<br>
	 * */
	// public String API_INSCKSTACK = getUrl() + "/api/pad/INSCKStack";
	/**
	 * 功能: 交换工程号<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：出库<br>
	 * 备注：<br>
	 * */
	public String API_SWAPPROJECTNO = getUrl() + "/api/pad/swapProjectno";
	/**
	 * 功能: 查询行车信息<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link CraneByType}<br>
	 * 画面：行车面板(这部分功能是pad独有的)<br>
	 * 备注：<br>
	 * */
	public String API_GET_WORKPANEL_CRANES = getUrl()
			+ "/api/pad/getDeviceIdHC";
	/**
	 * 功能: 查询行车作业列表信息<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link CraneWorkDetail}<br>
	 * 画面：行车作业面板<br>
	 * 备注：<br>
	 * */
	public String API_GET_BRICRANE_WORKPANEL = getUrl()
			+ "/api/pad/getBricraneworkpanel";
	/**
	 * 功能: 完成更新<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：行车作业面板<br>
	 * 备注：<br>
	 * */
	public String API_UPDATEBRICRANE_WORKPANEL = getUrl()
			+ "/api/pad/UpdateBricraneworkpanel";
	/**
	 * 功能: 根据库房类型查询库房信息 <br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link StoreHouse}<br>
	 * 画面：钢板库盘点<br>
	 * 备注：功能与{API_GET_HOUSEID}接口一致，可考虑合并<br>
	 * */
	public String API_GET_STORE_NAME = getUrl() + "/api/pad/getStoreName";
	/**
	 * 功能: 根据库房查询所有货位信息 <br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link Stack}<br>
	 * 画面：钢板库盘点<br>
	 * 备注：功能与{API_GET_HOUSEID}接口一致，可考虑合并<br>
	 * */
	public String API_GET_STOCK_CKECK_LIST = getUrl() + "/api/pad/getStackNoPD";
	/**
	 * 功能: 结束盘点<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link BaseModel}<br>
	 * 画面：钢板库盘点<br>
	 * 备注：可考虑与pc端合并，同帐号多次操作暂不影响<br>
	 * */
	public String API_STOCK_CHECK_END = getUrl() + "/api/pad/EndSheetFlag";
	/**
	 * 功能: 获取已经盘点信息<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：钢板库盘点<br>
	 * 备注：<br>
	 * */
	public String API_GETGATHERLIST = getUrl() + "/api/pad/getGatherList";
	/**
	 * 功能: 插入盘点信息<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：钢板库盘点<br>
	 * 备注：批量
	 * */
	public String API_ADDGATHER = getUrl() + "/api/pad/addGather";
	/**
	 * 功能: 清除垛位的盘点信息<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：钢板库盘点<br>
	 * 备注：<br>
	 * */
	public String API_CLEANGATHER = getUrl() + "/api/pad/clearGather";
	/**
	 * 功能: 根据库位编号获取库位下所有钢板<br>
	 * 传入值: <br>
	 * 返回值: 已入库钢板<br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：盘点明细<br>
	 * 备注：功能与{API_GET_SHEETLIST_BYSTACKNO}接口一致，可考虑合并<br>
	 */
	public String API_GET_WORKLIST_ANDINSTACK = getUrl()
			+ "/api/pad/getWorkListAndInStack";

	/**
	 * 功能: 根据库房编号、材质、长宽厚获取推荐垛位信息<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link Stack}<br>
	 * 画面：<br>
	 * 备注：API_GET_STACKINFO和这个接口是一样的,空垛位也可推荐<br>
	 * */
	public String API_GETSTACKINFONAME = getUrl() + "/api/pad/getStackInfoName";
	/**
	 * 功能: 勾选后获取行车信息 <br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link CraneByType}<br>
	 * 画面：<br>
	 * 备注：<br>
	 * */
	public String API_GET_CRANELIST = getUrl() + "/api/pad/getCraneList";
	/**
	 * 功能: 根据钢板条码、厚度、材质、长度、宽度、工程、分段、时间获取垛位和库房信息的接口<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link LoginJudge}<br>
	 * 画面：库位总览,信息查询<br>
	 * 备注：<br>
	 */
	public String API_GETSHEETNUM_INSTACK = getUrl()
			+ "/api/pad/GetSheetNumInSTACK";
	/**
	 * 功能: 根据钢板同类型定义表获取垛位和库房信息的接口<br>
	 * 排除了推荐钢板已锈蚀的情况（出库专用）<br>
	 * 传入值: <br>
	 * 返回值: list、unlist、totalList<br>
	 * 解析体:{@link Stack}<br>
	 * 画面：<br>
	 * 备注：<br>
	 */
	public String API_GETSHEETNUMINSTACKRUST = getUrl()
			+ "/api/pad/GetSheetNumInSTACKRust";
	/**
	 * 功能: 根据输入的条件查询符合条件的钢板<br>
	 * 传入值: <br>
	 * 返回值: 返回两个集合 (钢板明细中钢板高亮显示)<br>
	 * 解析体:{@link Sheet}<br>
	 * 画面：出库、钢板明细<br>
	 * 备注：<br>
	 */
	public String API_ALLSHEETLIST_BYSTACKNO = getUrl()
			+ "/api/pad/allSheetListByStackNo";
	/**
	 * 功能: 修改钢板是否锈蚀状态<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link BaseModel}<br>
	 * 画面：出库，钢板明细<br>
	 * 备注：<br>
	 */
	public String API_UPDATASHEETRUST = getUrl() + "/api/pad/updataSheetRust";
	/**
	 * 功能: 获取列表绑定<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link }<br>
	 * 画面：<br>
	 * 备注：<br>
	 */
	public String API_SHEETORDERLIST = getUrl() + "/api/Report/SheetorderList";
	/**
	 * 功能: 获取钢板采购单记录<br>
	 * 传入值: <br>
	 * 返回值: <br>
	 * 解析体:{@link }<br>
	 * 画面：<br>
	 * 备注：<br>
	 */
	public String API_GETTABLEHEAD = getUrl() + "/api/user/getTableHead";

	/** 所有操作正常(resultCode) */
	public static final int OK = 0;
	// 1000+业务逻辑问题
	/** 数据已存在，比如新建用户时，用户已经存在 */
	public static final int EXIsTS = 1000;
	/** 数据不存在，比如删除用户时，用户不存在 */
	public static final int NOT_EXISTS = 10001;
	/** API错误 */
	public static final int API_ERROR = 2000;
	/** 不支持的API，比如使用GET方法调用API */
	public static final int API_NOTSUPPORTED = 2001;
	/** API未定义 */
	public static final int API_NOTDEFINED = 2002;
	/** 错误的Josn格式 */
	public static final int WRONG_JSON_FORMAT = 2003;
	/** 版本无用 */
	public static final int INVALID_VERSION = 2004;
	/** 用户未认证登陆 */
	public static final int USER_NOTAUTHENTICATED = 3000;
	/** 用户无权限 */
	public static final int USER_NOTRIGHT = 3001;
	/** 签名错误 */
	public static final int WRONG_SIGNATURE = 3003;
	/** 接入系统无权限 */
	public static final int ACCESSSYSTEM_NORIGHT = 3004;
	/** 输入参数错误 */
	public static final int WRONG_PARAMETER = 600;
	/** 接口没找到 */
	public static final int CONNECT_404 = 404;

}
