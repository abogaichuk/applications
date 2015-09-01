import annotation.Client;
import annotation.Duplicate;
import annotation.Work;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @author abogaichuk
 */
public class AnnotationCreateTest {
    @Test
    public void test() throws NoSuchMethodException, IllegalAccessException {
        Work work = createWork();
        Class<?> clazz = work.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Duplicate.class))
                System.out.println(field.getName()+"    "+((Client) field.get(work)).getName());
        }
    }

    private Work createWork() {
        return new Work(new Client("im a main"), new Client("im a dupl 1"), new Client("im a dupl 2"));
    }
}
