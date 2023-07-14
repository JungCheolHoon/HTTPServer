//package kr.co.mz.tutorial.jdbc.exception;
//
//public class ExceptionTutorial {
//
//    private static String randomlyThrowsException() throws RuntimeException {
//
//        return "";
//    }
//
//    public static void main(String[] args) {
//        try {
//            System.out.println(randomlyThrowsException());
//        } catch (UnluckyException e) {
//            throw new SessionExpiredException(e);
//        } finally {
//            System.out.println("FINISHED!!!!");
//        }
//
//        System.out.println("After Try");
//    }
//}
