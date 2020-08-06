import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestCategory {

    @Test
    public void testCategory() throws IOException {
        File file = new File("/Users/nanamisasaki/Downloads/train.tsv");
        if (!file.exists()) {
            System.out.print("ファイルが存在しません");
            return;
        }

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        File f = new File("/Users/nanamisasaki/IdeaProjects/category/category.txt");
        if (f.exists()) {
            f.delete();
        } else {
            f.createNewFile();
        }

        String data;
        List<String> categories = new ArrayList<>();
        while ((data = bufferedReader.readLine()) != null) {
            String category_name = data.split("\t")[3];

            if (!categories.contains(category_name)) {
                categories.add(category_name);
            }
        }
        FileWriter fw = new FileWriter(f);
        Collections.sort(categories);
        for (String cate : categories) {
            fw.write(cate + System.getProperty("line.separator"));
        }

        // 最後にファイルを閉じてリソースを開放する
        bufferedReader.close();
        fw.close();
    }

    @Test
    public void create_category_insert_table() throws Exception {
        File f = new File("/Users/nanamisasaki/IdeaProjects/category/category.txt");
        FileReader fileReader = new FileReader(f);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        File insertFile = new File("/Users/nanamisasaki/IdeaProjects/category/insert.sql");

        String data;
        int ancestorId = 0;
        int descendantId = 0;
        String beforeAncestorName = "";
        String beforeDescendantName = "";
        FileWriter fw = new FileWriter(insertFile);

        while ((data = bufferedReader.readLine()) != null) {
            String ancestor = data.split("/")[0];
            String descendant = data.split("/")[1];
            System.out.println("before ancestor: " + beforeAncestorName + ", now ancestor: " + ancestor);

            if (beforeAncestorName.equals(ancestor)) {
                System.out.println("前回と大カテゴリーが一緒");
                System.out.println("before ancestor: " + beforeAncestorName + ", now ancestor: " + ancestor);

                if (!beforeDescendantName.equals(descendant)) {
                    System.out.println("前回と中カテゴリーが一緒");
                    System.out.println("before descendant: " + beforeDescendantName + ", now descendant: " + descendant);

                    descendantId = descendantId + 1;
                    fw.write("INSERT INTO category VALUES (" + ancestorId + ", " + descendantId + " , " + 1 + ");" + System.getProperty("line.separator"));
                    descendantId = descendantId + 1;
                    fw.write("INSERT INTO category VALUES (" + ancestorId + ", " + descendantId + " , " + 2 + ");" + System.getProperty("line.separator"));
                } else {

                    System.out.println("中カテゴリーは前回と一緒");
                    System.out.println("before descendant: " + beforeDescendantName + ", now descendant: " + descendant);

                    descendantId = descendantId + 1;
                    fw.write("INSERT INTO category VALUES (" + ancestorId + ", " + descendantId + " , " + 2 + ");" + System.getProperty("line.separator"));
                }
            } else {
                System.out.println("新しい大カテゴリーの登場");
                System.out.println("before ancestor: " + beforeAncestorName + ", now ancestor: " + ancestor);

                ancestorId = ancestorId + 1;

                System.out.println("新しい中カテゴリーの登場");
                System.out.println("before descendant: " + beforeDescendantName + ", now descendant: " + descendant);

                descendantId = descendantId + 1;
                fw.write("INSERT INTO category VALUES (" + ancestorId + ", " + descendantId + " , " + 1 + ");" + System.getProperty("line.separator"));
                descendantId = descendantId + 1;
                fw.write("INSERT INTO category VALUES (" + ancestorId + ", " + descendantId + " , " + 2 + ");" + System.getProperty("line.separator"));
            }
            beforeAncestorName = ancestor;
            beforeDescendantName = descendant;

        }
        // 最後にファイルを閉じてリソースを開放する
        bufferedReader.close();
        fw.close();

    }
}

