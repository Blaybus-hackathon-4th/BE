package com.example.blaybus4th.domain.aiChat.tool;

public class MemberContextHolder {

    private static final ThreadLocal<String> MEMBER_ID = new ThreadLocal<>();

    public static void set(String memberId){
        MEMBER_ID.set(String.valueOf(memberId));
    }

    public static String get(){
        return MEMBER_ID.get();
    }

    public static void clear(){
        MEMBER_ID.remove();
    }


}
