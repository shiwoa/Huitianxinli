package com.yizhilu.utils;

/**
 * @author 杨财宾 修改人: 时间:2015-8-29 下午1:37:10 类说明:接口地址的类
 */
public class ExamAddress {
	//域名
	public static String HOST = "http://exam.huitianedu.com/";
	//用户域名
	public static String USER_HOST = "www.huitianedu.com/app/";
	//图片域名
	public static String IMAGE_HOST = "http://static.huitianedu.com";
	// 登录接口
	public static String LOGIN = USER_HOST + "login";
	//注册类型的接口
	public static String REGIST_TYPE = USER_HOST + "registerType";
	//获取邮箱或手机号接受验证码开关的接口
	public static String GETCODESWITCH = USER_HOST + "emailMobileCodeSwitch";
	// 注册接口
	public static String REGISTER = USER_HOST + "register";
	//获取手机验证码
	public static String GET_PHONE_CODE = USER_HOST + "sendMobileMessage";
	//获取sgin
	public static String GET_SGIN = USER_HOST + "getMobileKey";
	//获取邮箱的验证码
	public static String GET_EMAIL_CODE = USER_HOST + "sendEmailMessage";
	//专业的接口
	public static String SUBLIST_URL = HOST+"app/exam/sub";
	//能力评估
	public static String ABILITYASSESS_URL = HOST +"app/exam/competentAssessment";
	//联网获取练习历史的方法
	public static String PRACTICEHISTORY_URL = HOST+"app/exam/toExamPaperRecordList";
	//获取错误习题的方法
	public static String ERROREXERCISE_URL = HOST+"app/exam/errqst";
	//获取收藏试题的接口
	public static String COLLECTEXERCISE_URL = HOST+"app/exam/collectqst";
	//最近未做完的试题
	public static String UNFINISHED_URL = HOST+"app/exam/recentlyNotFinishPaper";
	//移除错题的方法
	public static String REMOVEERRORTI_URL = HOST + "app/exam/delqst";
	//获取单个试题解析的方法
	public static String LOOKSINGLEPARSER_URL = HOST +"app/exam/qstanalysis";
	//获取阶段测试的列表的接口
	public static String PHASETESTLIST_URL = HOST+"app/exam/queryPaperListByType";
	//获取论述题自测的数量
	public static String DIACUSSTESTNUM_URL = HOST +"app/exam/paper/queryQuestionNum";
	//知识点接口
	public static String KNOWLEDGEPOINT_URL = HOST+"app/exam/queryPointList";
	//知识点中，再来15道，顺序练习的接口
	public static String KNOWLEDGEPOINT_PRACTICEURL = HOST +"app/exam/getRandomQuestionByPointIds";
	//论述题自测的接口
	public static String DIACUSSTEST_URL = HOST+"app/exam/getQuestionTestPaper";
	//组卷模考的接口
	public static String ZUJUANEXAM_URL=HOST+"app/exam/getZujuanPaper";
	//错题智能练习的的接口
	public static String CAPACITY_ERROR_URL = HOST+"app/exam/getRandomQuestion";
	//取消收藏的接口
	public static String CANCELCOLLECT_URL = HOST+"app/exam/notFavorite";
	//提交纠错的接口
	public static String SUBMITCORRECTION_URL = HOST+"app/exam/addQuestErrorCheck";
	//收藏的接口
	public static String COLLECT_URL = HOST+"app/exam/toFavorite";
	//查看解析和继续考试的接口
	public static String LOOKPARSERORCONTINUEPRACTICE = HOST+"app/exam/toExamPaperRecord";
	//获取阶段测试和真题练习的试题
	public static String PHASETEST_EXAMURL = HOST+"app/exam/toExamPaper";
	//提交试卷的接口
	public static String SUBMIT_EXAMPAPER_URL = HOST+"app/exam/addPaperRecord";
	//练习报告的接口
	public static String PRACTICEREPORT_URL = HOST+"app/exam/getExamPaperReport";
	//联网获取每个考点的练习情况
	public static String PRACTICECONDITION_URL = HOST+"app/exam/queryPointAndQuestionRecordListByCusId";
	//添加笔记
	public static String ADDNOTE_URL = HOST + "app/exam/addnote";
}
