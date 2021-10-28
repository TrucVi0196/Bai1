import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class main {
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    public static void main(String[] args) throws IOException {

        // 1. Đọc danh sách sinh viên từ tập tin data.csv được cung cấp

        List<SinhVien> sv = processInputFile("C:\\Users\\vi.le_onemount\\Documents\\QLSV\\src\\data.txt");
        //2. Liệt kê danh sách 10 sinh viên có điểm thi lý thuyết cao nhất
        List<SinhVien> svCaodiem = (sv.stream().sorted(Comparator.comparing(SinhVien::getlT).reversed()).collect(Collectors.toList()));
        print10(svCaodiem);

        //3. Tính điểm tổng kết cho từng sinh viên theo công thức: bonus 10%,
        //report 30%, app 15%, lý thuyết 45%; điểm tổng kết được làm tròn đến
        //0.5 (ví dụ: 7.37 -> 7.5, 6.2 -> 6.0)
        sv = sv.stream().map(x -> tinhTongDiem(x)).collect(Collectors.toList());
        print10(sv.stream().sorted(Comparator.comparing(SinhVien::getDiemTongKet).reversed()).collect(Collectors.toList()));
        writeCsvFile(sv, "C:\\Users\\vi.le_onemount\\Documents\\QLSV\\src\\out.csv");


    }

    private static void writeCsvFile(List<SinhVien> sv, String fileName) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            // Write the CSV file header
            fileWriter.append("ID,Name,Email,Bonus,Report,App,LT,DiemTongKet");

            // Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            // Write a new Country object list to the CSV file
            for (SinhVien s : sv) {
                fileWriter.append(String.valueOf(s.getiD()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(s.getName());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(s.getEmail());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(s.getBonus()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(s.getReport()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(s.getApp()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(s.getlT()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(s.getDiemTongKet()));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }

    private static void print10(List<SinhVien> sv) {
        int nu = 10;
        if (sv.size() < nu)
            nu = sv.size();
        for (int i = 0; i < nu; i++) {
            System.out.println(sv.get(i).getName());
        }
    }

    private static SinhVien tinhTongDiem(SinhVien sv) {


        // different locale - GERMAN

        double diem = sv.getBonus() * 0.1 + sv.getReport() * 0.3 + sv.getApp() * 0.15 + sv.getlT() * 0.45;
        sv.setDiemTongKet(diem);
        return sv;
    }

    private static List<SinhVien> processInputFile(String inputFilePath) throws IOException {

        List<SinhVien> inputList = new ArrayList<SinhVien>();

        try {

            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

            // skip the header of the csv
            inputList = br.lines().skip(1).map(x -> mapToSinhVien(x)).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            throw e;
        }

        return inputList;
    }

    private static SinhVien mapToSinhVien(String line) {
        String[] p = line.split(",");
        SinhVien sv = new SinhVien();
        sv.setiD(Long.parseLong(p[0]));
        sv.setName(p[1]);
        sv.setEmail(p[2]);
        sv.setBonus(Integer.parseInt(p[3]));
        sv.setReport(Integer.parseInt(p[4]));
        sv.setApp(Integer.parseInt(p[5]));
        sv.setlT(Double.parseDouble(p[6]));
        return sv;
    }
}

//D,Name,Email,Bonus,Report,App,LT
class SinhVien {
    private Long iD;
    private String name;
    private String email;
    private Integer bonus;
    private Integer report;
    private Integer app;
    private Double lT;
    private Double diemTongKet;

    public Double getDiemTongKet() {
        return diemTongKet;
    }

    public void setDiemTongKet(Double diemTongKet) {
        this.diemTongKet = diemTongKet;
    }

    public Long getiD() {
        return iD;
    }

    public void setiD(Long iD) {
        this.iD = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public Integer getReport() {
        return report;
    }

    public void setReport(Integer report) {
        this.report = report;
    }

    public Integer getApp() {
        return app;
    }

    public void setApp(Integer app) {
        this.app = app;
    }

    public Double getlT() {
        return lT;
    }

    public void setlT(Double lT) {
        this.lT = lT;
    }
}
