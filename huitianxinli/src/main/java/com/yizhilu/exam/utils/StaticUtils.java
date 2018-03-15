package com.yizhilu.exam.utils;

import java.util.List;

/**
 * 作者：caibin
 * 时间：2016/6/4 18:06
 * 类说明：静态类
 */
public class StaticUtils {
    public static List<String> answerList;  //答案的集合

    public static List<String> getNoteList() {
        return noteList;
    }

    public static void setNoteList(List<String> noteList) {
        StaticUtils.noteList = noteList;
    }

    public static List<String> noteList;  //笔记的集合
    public static List<String> getAnswerList() {
        return answerList;
    }

    public static void setAnswerList(List<String> answerList) {
        StaticUtils.answerList = answerList;
    }

    public static void setPositionAnswer(int position,String answer){
        StaticUtils.answerList.set(position,answer);
    }

    public static String getPositionAnswer(int position){
        return StaticUtils.answerList.get(position);
    }
}
