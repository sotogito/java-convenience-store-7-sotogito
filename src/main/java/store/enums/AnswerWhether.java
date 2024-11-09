package store.enums;

import store.enums.messages.ErrorMessage;

public enum AnswerWhether {
    YES("Y", true),
    NO("N", false);

    private final String answer;
    private final boolean meaning;

    AnswerWhether(String answer, boolean meaning1) {
        this.answer = answer;
        this.meaning = meaning1;
    }

    public static boolean isYes(AnswerWhether userAnswer) {
        for (AnswerWhether answerWhether : AnswerWhether.values()) {
            if (answerWhether.equals(userAnswer)) {
                return answerWhether.meaning;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.INCORRECT_INPUT.get());
    }


    public static AnswerWhether find(String inputAnswer) {
        for (AnswerWhether answerWhether : AnswerWhether.values()) {
            if (answerWhether.answer.equals(inputAnswer)) {
                return answerWhether;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.INCORRECT_INPUT.get());
    }

}
