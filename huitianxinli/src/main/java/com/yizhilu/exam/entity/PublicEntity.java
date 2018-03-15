package com.yizhilu.exam.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 共工实体
 * Created by yzl on 2016/5/24.
 */
public class PublicEntity implements Serializable{
    private String message;
    private boolean success;
    private PublicEntity entity;
    private int id;
    private String nickname;
    private String email;
    private int emailIsavalible;
    private String mobile;
    private int mobileIsavalible;
    private String password;
    private int isavalible;
    private String customerkey;
    private String createdate;
    private String userip;
    private int userType;
    private int averageScore;
    private int qstNum;
    private int errorQstNum;
    private int averageScoreRanking;
    private int rightQstNum;
    private String memTime;
    private PublicEntity user;
    private String keyType;
    private String verifyRegEmailCode;
    private String verifyRegMobileCode;
    private int professionalId;
    private String professionalName;
    private int status;
    private String createTime;
    private String updateTime;
    private int sort;
    private List<PublicEntity> subjectList;
    private int subjectId;
    private String subjectName;
    private PublicEntity page;
    private List<PublicEntity> queryPaperRecordList;
    private int totalResultSize;
    private int totalPageSize;
    private int pageSize;
    private int currentPage;
    private int startRow;
    private boolean first;
    private boolean last;
    private String paperName;
    private String addTime;
    private List<PublicEntity> queryQuestionList;
    //错题记录
    private String qstContent;
    private String isAsr;
    private int qstType;
    private int level;
    private String qstAnalyze;
    private int pointId;
    private String author;
    private int complexFalg;
    private String flag;
    private int qstRecordstatus;
    private int errorQuestionId;
    private String errorQuestionAddTime;
    private int placeNumber;
    private int paperRecordId;
    private int time;
    private int rightTime;
    private int errorTime;
    private int score;
    private int userscore;
    private int state;
    private String shortQstContent;
    private String section;
    private String  extendContentType;
    private String contentAddress;
    private List<String> fillList;
    private List<String> userFillList;
    private String favTime;
    //最近练习
    private PublicEntity recentlyNotFinishPaper;
    private PublicEntity queryQuestion;
    //阶段测试
    private List<PublicEntity> paperList;
    private String title;
    private String name;
    private String info;
    private int replyTime;
    private int joinNum;
    private String avgScore;
    private int type;
    private int qstCount;
    private String startTime;
    private String endTime;
    private int passNum;
    private int testNumber;
    private int count;
    private List<PublicEntity> pointList;
    private String paperTitle;
    private List<PublicEntity> examPointSon;
    private int parentId;
    private int examFrequency;
    private int errorQstCount;
    private int t_type;
    private int testTime;
    private String msg;
    private int favoritesId;
    private List<PublicEntity> options;
    private String optContent;
    private String optOrder;
    private int doNum;
    private String userAnswer;
    private List<PublicEntity> paperMiddleList;
    private List<PublicEntity> complexList;
    private String complexContent;
    private List<PublicEntity> queryQstMiddleList;
    private PublicEntity paperRecord;
    private PublicEntity paper;
    private int complexId;
    private int correctNum;
    private float accuracy;
    private int cusRightQstNum;
    private List<PublicEntity> qstMiddleList;
    private int qstId;
    private int paperMiddleId;
    private int questionType;
    private List<PublicEntity> errorQuestionList;
    private String noteContent;
    private int questionZong;  //试题类型总的题数
    private String complexType;  //记录试卷材料题下面的各个小题
    private int questionPosition;  //试题类型当前题数
    private int paperType;
    public String getComplexType() {
        return complexType;
    }
    public void setComplexType(String complexType) {
        this.complexType = complexType;
    }

    public int getQuestionPosition() {
        return questionPosition;
    }

    public void setQuestionPosition(int questionPosition) {
        this.questionPosition = questionPosition;
    }

    public int getQuestionZong() {
        return questionZong;
    }

    public void setQuestionZong(int questionZong) {
        this.questionZong = questionZong;
    }
    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public List<PublicEntity> getErrorQuestionList() {
        return errorQuestionList;
    }

    public void setErrorQuestionList(List<PublicEntity> errorQuestionList) {
        this.errorQuestionList = errorQuestionList;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public int getPaperMiddleId() {
        return paperMiddleId;
    }

    public void setPaperMiddleId(int paperMiddleId) {
        this.paperMiddleId = paperMiddleId;
    }

    public int getQstId() {
        return qstId;
    }

    public void setQstId(int qstId) {
        this.qstId = qstId;
    }

    public List<PublicEntity> getQstMiddleList() {
        return qstMiddleList;
    }

    public void setQstMiddleList(List<PublicEntity> qstMiddleList) {
        this.qstMiddleList = qstMiddleList;
    }

    public int getCusRightQstNum() {
        return cusRightQstNum;
    }

    public void setCusRightQstNum(int cusRightQstNum) {
        this.cusRightQstNum = cusRightQstNum;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public int getCorrectNum() {
        return correctNum;
    }

    public void setCorrectNum(int correctNum) {
        this.correctNum = correctNum;
    }

    public int getComplexId() {
        return complexId;
    }

    public void setComplexId(int complexId) {
        this.complexId = complexId;
    }

    public PublicEntity getPaper() {
        return paper;
    }

    public void setPaper(PublicEntity paper) {
        this.paper = paper;
    }

    public PublicEntity getPaperRecord() {
        return paperRecord;
    }

    public void setPaperRecord(PublicEntity paperRecord) {
        this.paperRecord = paperRecord;
    }

    public List<PublicEntity> getQueryQstMiddleList() {
        return queryQstMiddleList;
    }

    public void setQueryQstMiddleList(List<PublicEntity> queryQstMiddleList) {
        this.queryQstMiddleList = queryQstMiddleList;
    }

    public String getComplexContent() {
        return complexContent;
    }

    public void setComplexContent(String complexContent) {
        this.complexContent = complexContent;
    }

    public List<PublicEntity> getComplexList() {
        return complexList;
    }

    public void setComplexList(List<PublicEntity> complexList) {
        this.complexList = complexList;
    }

    public List<PublicEntity> getPaperMiddleList() {
        return paperMiddleList;
    }

    public void setPaperMiddleList(List<PublicEntity> paperMiddleList) {
        this.paperMiddleList = paperMiddleList;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getOptContent() {
        return optContent;
    }

    public void setOptContent(String optContent) {
        this.optContent = optContent;
    }

    public String getOptOrder() {
        return optOrder;
    }

    public void setOptOrder(String optOrder) {
        this.optOrder = optOrder;
    }

    public int getDoNum() {
        return doNum;
    }

    public void setDoNum(int doNum) {
        this.doNum = doNum;
    }

    public List<PublicEntity> getOptions() {
        return options;
    }

    public void setOptions(List<PublicEntity> options) {
        this.options = options;
    }

    public int getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(int favoritesId) {
        this.favoritesId = favoritesId;
    }

    public int getT_type() {
        return t_type;
    }

    public void setT_type(int t_type) {
        this.t_type = t_type;
    }

    public int getTestTime() {
        return testTime;
    }

    public void setTestTime(int testTime) {
        this.testTime = testTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<PublicEntity> getPointList() {
        return pointList;
    }

    public void setPointList(List<PublicEntity> pointList) {
        this.pointList = pointList;
    }

    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }

    public List<PublicEntity> getExamPointSon() {
        return examPointSon;
    }

    public void setExamPointSon(List<PublicEntity> examPointSon) {
        this.examPointSon = examPointSon;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getExamFrequency() {
        return examFrequency;
    }

    public void setExamFrequency(int examFrequency) {
        this.examFrequency = examFrequency;
    }

    public int getErrorQstCount() {
        return errorQstCount;
    }

    public void setErrorQstCount(int errorQstCount) {
        this.errorQstCount = errorQstCount;
    }

    public List<PublicEntity> getPaperList() {
        return paperList;
    }

    public void setPaperList(List<PublicEntity> paperList) {
        this.paperList = paperList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(int replyTime) {
        this.replyTime = replyTime;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    public String getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(String avgScore) {
        this.avgScore = avgScore;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getQstCount() {
        return qstCount;
    }

    public void setQstCount(int qstCount) {
        this.qstCount = qstCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public PublicEntity getQueryQuestion() {
        return queryQuestion;
    }

    public void setQueryQuestion(PublicEntity queryQuestion) {
        this.queryQuestion = queryQuestion;
    }

    public PublicEntity getRecentlyNotFinishPaper() {
        return recentlyNotFinishPaper;
    }

    public void setRecentlyNotFinishPaper(PublicEntity recentlyNotFinishPaper) {
        this.recentlyNotFinishPaper = recentlyNotFinishPaper;
    }

    public List<String> getFillList() {
        return fillList;
    }

    public void setFillList(List<String> fillList) {
        this.fillList = fillList;
    }

    public List<String> getUserFillList() {
        return userFillList;
    }

    public void setUserFillList(List<String> userFillList) {
        this.userFillList = userFillList;
    }

    public String getFavTime() {
        return favTime;
    }

    public void setFavTime(String favTime) {
        this.favTime = favTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQstContent() {
        return qstContent;
    }

    public void setQstContent(String qstContent) {
        this.qstContent = qstContent;
    }

    public String getIsAsr() {
        return isAsr;
    }

    public void setIsAsr(String isAsr) {
        this.isAsr = isAsr;
    }

    public int getQstType() {
        return qstType;
    }

    public void setQstType(int qstType) {
        this.qstType = qstType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getQstAnalyze() {
        return qstAnalyze;
    }

    public void setQstAnalyze(String qstAnalyze) {
        this.qstAnalyze = qstAnalyze;
    }

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public int getComplexFalg() {
        return complexFalg;
    }

    public void setComplexFalg(int complexFalg) {
        this.complexFalg = complexFalg;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getQstRecordstatus() {
        return qstRecordstatus;
    }

    public void setQstRecordstatus(int qstRecordstatus) {
        this.qstRecordstatus = qstRecordstatus;
    }

    public int getErrorQuestionId() {
        return errorQuestionId;
    }

    public void setErrorQuestionId(int errorQuestionId) {
        this.errorQuestionId = errorQuestionId;
    }

    public String getErrorQuestionAddTime() {
        return errorQuestionAddTime;
    }

    public void setErrorQuestionAddTime(String errorQuestionAddTime) {
        this.errorQuestionAddTime = errorQuestionAddTime;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }

    public int getPaperRecordId() {
        return paperRecordId;
    }

    public void setPaperRecordId(int paperRecordId) {
        this.paperRecordId = paperRecordId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getRightTime() {
        return rightTime;
    }

    public void setRightTime(int rightTime) {
        this.rightTime = rightTime;
    }

    public int getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(int errorTime) {
        this.errorTime = errorTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUserscore() {
        return userscore;
    }

    public void setUserscore(int userscore) {
        this.userscore = userscore;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getShortQstContent() {
        return shortQstContent;
    }

    public void setShortQstContent(String shortQstContent) {
        this.shortQstContent = shortQstContent;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getExtendContentType() {
        return extendContentType;
    }

    public void setExtendContentType(String extendContentType) {
        this.extendContentType = extendContentType;
    }

    public String getContentAddress() {
        return contentAddress;
    }

    public void setContentAddress(String contentAddress) {
        this.contentAddress = contentAddress;
    }

    public List<PublicEntity> getQueryQuestionList() {
        return queryQuestionList;
    }

    public void setQueryQuestionList(List<PublicEntity> queryQuestionList) {
        this.queryQuestionList = queryQuestionList;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getTotalResultSize() {
        return totalResultSize;
    }

    public void setTotalResultSize(int totalResultSize) {
        this.totalResultSize = totalResultSize;
    }

    public int getTotalPageSize() {
        return totalPageSize;
    }

    public void setTotalPageSize(int totalPageSize) {
        this.totalPageSize = totalPageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public PublicEntity getPage() {
        return page;
    }

    public void setPage(PublicEntity page) {
        this.page = page;
    }

    public List<PublicEntity> getQueryPaperRecordList() {
        return queryPaperRecordList;
    }

    public void setQueryPaperRecordList(List<PublicEntity> queryPaperRecordList) {
        this.queryPaperRecordList = queryPaperRecordList;
    }

    public PublicEntity getEntity() {
        return entity;
    }

    public void setEntity(PublicEntity entity) {
        this.entity = entity;
    }

    public String getMemTime() {
        return memTime;
    }

    public void setMemTime(String memTime) {
        this.memTime = memTime;
    }

    public PublicEntity getUser() {
        return user;
    }

    public void setUser(PublicEntity user) {
        this.user = user;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getVerifyRegEmailCode() {
        return verifyRegEmailCode;
    }

    public void setVerifyRegEmailCode(String verifyRegEmailCode) {
        this.verifyRegEmailCode = verifyRegEmailCode;
    }

    public String getVerifyRegMobileCode() {
        return verifyRegMobileCode;
    }

    public void setVerifyRegMobileCode(String verifyRegMobileCode) {
        this.verifyRegMobileCode = verifyRegMobileCode;
    }

    public int getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(int professionalId) {
        this.professionalId = professionalId;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<PublicEntity> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<PublicEntity> subjectList) {
        this.subjectList = subjectList;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmailIsavalible() {
        return emailIsavalible;
    }

    public void setEmailIsavalible(int emailIsavalible) {
        this.emailIsavalible = emailIsavalible;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getMobileIsavalible() {
        return mobileIsavalible;
    }

    public void setMobileIsavalible(int mobileIsavalible) {
        this.mobileIsavalible = mobileIsavalible;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsavalible() {
        return isavalible;
    }

    public void setIsavalible(int isavalible) {
        this.isavalible = isavalible;
    }

    public String getCustomerkey() {
        return customerkey;
    }

    public void setCustomerkey(String customerkey) {
        this.customerkey = customerkey;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getUserip() {
        return userip;
    }

    public void setUserip(String userip) {
        this.userip = userip;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(int averageScore) {
        this.averageScore = averageScore;
    }

    public int getQstNum() {
        return qstNum;
    }

    public void setQstNum(int qstNum) {
        this.qstNum = qstNum;
    }

    public int getErrorQstNum() {
        return errorQstNum;
    }

    public void setErrorQstNum(int errorQstNum) {
        this.errorQstNum = errorQstNum;
    }

    public int getAverageScoreRanking() {
        return averageScoreRanking;
    }

    public void setAverageScoreRanking(int averageScoreRanking) {
        this.averageScoreRanking = averageScoreRanking;
    }

    public int getRightQstNum() {
        return rightQstNum;
    }

    public void setRightQstNum(int rightQstNum) {
        this.rightQstNum = rightQstNum;
    }
	public int getPaperType() {
		return paperType;
	}
	public void setPaperType(int paperType) {
		this.paperType = paperType;
	}
}
