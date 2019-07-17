package mvnikitin.javaadvanced.lesson2;

enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURTHDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

public class DayOfWeekMain {

    private static final int WORKHOURS_TOTAL = 40;
    private static final int WORKHOURS_PER_DAY = 8;

    public static void main(String[] args) {
        for (DayOfWeek dw: DayOfWeek.values()) {
            int workHoursLeft = getWorkingHours(dw);
            System.out.println(dw + ": " +
                    (workHoursLeft > 0 ? workHoursLeft : "выходной день"));
        }
    }

    private static int getWorkingHours(DayOfWeek dayOfWeek) {
        int res = 0;

        switch (dayOfWeek) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURTHDAY:
            case FRIDAY:
                res = WORKHOURS_TOTAL -
                        dayOfWeek.ordinal() * WORKHOURS_PER_DAY;
                break;
            default:
              res = 0;
        }

        return res;
    }
}
