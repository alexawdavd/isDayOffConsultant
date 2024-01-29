package com.example.demo.controller;

import com.example.demo.service.HtmlParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("getDaysOff")
@AllArgsConstructor
@Tag(name = "Выходные дни", description = "")
public class DaysOffController {

    @Operation(summary = "Список праздничных дней")
    @GetMapping()
    public List<LocalDate> getContractInfo(int year) {
        try {
            HtmlParser parser = new HtmlParser();
            return parser.getDaysOffDates(year);
        } catch (Exception e) {
            return null;
        }

    }

    @Operation(summary = "Праздничный ли день по дате")
    @GetMapping("/isDayOffByDate")
    public int isDayOffByDate(int year, int month, int day) {
        try {
            HtmlParser parser = new HtmlParser();
            List<LocalDate> daysOff = null;
            LocalDate date = null;
            try {
                 daysOff = parser.getDaysOffDates(year);
            } catch (Exception e) { return 101; }
            try {
                date = LocalDate.of(year, month, day);
            } catch (Exception e) { return 100; }

            if (daysOff.contains(date)){
                return 1;
            }
            else {
                return 0;
            }

        } catch (Exception e) {
            return -1;
        }

    }


    @Operation(summary = "Праздничный ли день сегодня")
    @GetMapping("/isDayOffTodayMOSCOW")
    public int isDayOffToday() {
        try {
            HtmlParser parser = new HtmlParser();
            List<LocalDate> daysOff = null;
            LocalDate date = LocalDate.now();
            try {
                daysOff = parser.getDaysOffDates(LocalDate.now().getYear());
            } catch (Exception e) { return 101; }

            if (daysOff.contains(date)){
                return 1;
            }
            else {
                return 0;
            }

        } catch (Exception e) {
            return -1;
        }

    }

}
