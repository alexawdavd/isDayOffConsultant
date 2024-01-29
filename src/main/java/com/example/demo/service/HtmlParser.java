package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HtmlParser {

    private static int monthNameToInt(String monthName) {

        ArrayList<String> month = new ArrayList<String>();
        month.add("Январь");
        month.add("Февраль");
        month.add("Март");
        month.add("Апрель");
        month.add("Май");
        month.add("Июнь");
        month.add("Июль");
        month.add("Август");
        month.add("Сентябрь");
        month.add("Октябрь");
        month.add("Ноябрь");
        month.add("Декабрь");
        try {
            return month.indexOf(monthName) + 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static String extractMonthName(String html) {
        Document doc = Jsoup.parse(html);
        Element monthElement = doc.select("th.month").first();
        return monthElement.text();
    }

    private static String getHtmlCode(int year){
        try {
            String url = "https://www.consultant.ru/law/ref/calendar/proizvodstvennye/"+ year +"/";

            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

    public List<LocalDate> getDaysOffDates(int year) {

        Document doc = Jsoup.parse(getHtmlCode(year));

        Elements tables = doc.select("table.cal");
        List<String> calTableHtmlList = new ArrayList<>();
        for (Element table : tables) {
            calTableHtmlList.add(table.outerHtml());
        }

        List<LocalDate> holidayWeekendDates = new ArrayList<>();

        calTableHtmlList.forEach(cal -> {
            Document calDoc = Jsoup.parse(cal);
            Elements cells = calDoc.select("tbody td.holiday, tbody td.weekend");


            for (Element cell : cells) {
                String day = cell.text();
                int month = monthNameToInt(extractMonthName(calDoc.html()));
                int dayOfMonth = Integer.parseInt(day);
                holidayWeekendDates.add(LocalDate.of(year, month, dayOfMonth));
            }

        });

        return holidayWeekendDates;

    }
}