//package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto;
//
//import id.ac.ui.cs.advprog.b14.pandacare.scheduling.model.WorkingSchedule;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.format.DateTimeFormatter;
//
//@Getter
//@Setter
//@Builder
//public class WorkingScheduleDTO {
//    private Long id; // Added id field
//    private String startTime;
//    private String endTime;
//    private String status;
//    private boolean available;
//
//    public static WorkingScheduleDTO fromEntity(WorkingSchedule entity) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
//
//        return WorkingScheduleDTO.builder()
//                .id(Long.valueOf(entity.getId())) // Added id mapping
//                .startTime(entity.getStartTime().format(formatter))
//                .endTime(entity.getEndTime().format(formatter))
//                .status(entity.getStatus())
//                .available(entity.isAvailable())
//                .build();
//    }
//}