package com.hg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.Attendance;
import com.hg.model.AttendanceDto;


public interface AttendanceDao extends PagingAndSortingRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance>{

	
	@Query("select new com.hg.model.AttendanceDto("
			+ "a.id,a.attendanceDay,a.weight,a.lengthenHour,a.otherHour,a.tool,a.qualityPrize,b.userName,b.salary,"
			+ "c.groupName,c.unitPrice,a.seniorityPay,a.attendancePay,a.piecePay,a.restDay,a.restPay,a.otherPay,"
			+ "a.safetyPrize,a.attendanceBonus,a.examineMoney,a.total,a.dueDay) from Attendance a,Employee b,Group c where a.emplId=b.id and b.groupId=c.id ")
	public List<AttendanceDto> findAttendances();
	
	
	public List<Attendance> findByEmplId(Long emplId);
}
