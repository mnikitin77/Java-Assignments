package mvnikitin.javaadvanced.lesson3;

interface PWDStrength {
    public boolean checkPWDStrength(String pwd);
    public String checkInfo();
}

class PWDDigitCheck implements PWDStrength {

    @Override
    public boolean checkPWDStrength(String pwd) {
        boolean res = false;
        String charsToCheck = "0123456789";

        for (int i = 0; i < pwd.length(); i++)
        {
            if (charsToCheck.indexOf(pwd.charAt(i)) != -1) {
                res = true;
                break;
            }
        }

        return res;
    }

    @Override
    public String checkInfo() {
        return "Проверка наличия цифры: ";
    }
}

class PWDLengthCheck implements PWDStrength {

    @Override
    public boolean checkPWDStrength(String pwd) {
        return pwd.length() >= 8 && pwd.length() <= 20;
    }

    @Override
    public String checkInfo() {
        return "Проверка длины от 8 до 20 символов: ";
    }
}

class PWDCaseCheck implements PWDStrength {

    @Override
    public boolean checkPWDStrength(String pwd) {
        boolean upperFound = false;
        boolean lowerFund = false;
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < pwd.length(); i++)
        {
            if (!upperFound && upperCase.indexOf(pwd.charAt(i)) != -1) {
                upperFound = true;
            }
            if (!lowerFund && lowerCase.indexOf(pwd.charAt(i)) != -1) {
                lowerFund = true;
            }
            if(upperFound && lowerFund)
                break;
        }

        return upperFound && lowerFund;
    }

    @Override
    public String checkInfo() {
        return "Проверка наличия приписных и строчных букв: ";
    }
}

class PWDSpecCharCheck implements PWDStrength {

    @Override
    public boolean checkPWDStrength(String pwd) {
        boolean res = false;
        String charsToCheck = "`~!@#$%^&*()_-+={}[]\\|:;\"'<>,.?/";

        for (int i = 0; i < pwd.length(); i++)
        {
            if (charsToCheck.indexOf(pwd.charAt(i)) != -1) {
                res = true;
                break;
            }
        }

        return res;
    }

    @Override
    public String checkInfo() {
        return "Проверка наличия спецсимвола: ";
    }
}


public class PWDStrengthChecker {

    private PWDStrength[] checks;

    public PWDStrengthChecker() {
        checks = new PWDStrength[4];
        checks[0] = new PWDDigitCheck();
        checks[1] = new PWDLengthCheck();
        checks[2] = new PWDCaseCheck();
        checks[3] = new PWDSpecCharCheck();
    }

    public void checkPWDStrength(String pwd) {
        System.out.println("Проверка пароля " + pwd);

        for (int i = 0; i < checks.length; i++) {
            System.out.print(checks[i].checkInfo());
            if (checks[i].checkPWDStrength(pwd)) {
                System.out.println("PASS");
            }
            else {
                System.out.println("FAIL");
                break;
            }
        }

        System.out.println();
    }

    public static void main(String[] args) {

        PWDStrengthChecker pwdChecker = new PWDStrengthChecker();

        pwdChecker.checkPWDStrength("Qwerty123$");
        pwdChecker.checkPWDStrength("abc");
        pwdChecker.checkPWDStrength("abcacba@cbaCBAcbacba100500cbacbabc=-abcabcacb");
        pwdChecker.checkPWDStrength("helloworld12345");
        pwdChecker.checkPWDStrength("Helloworldddddd");
        pwdChecker.checkPWDStrength("Helloworl100500");
    }
}
