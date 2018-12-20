package com.hg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hg.model.Employee;
import com.hg.model.GroupVoEmpl;


public interface EmployeeDao extends PagingAndSortingRepository<Employee, Long>, JpaSpecificationExecutor<Employee>{

	@Query("select new com.hg.model.GroupVoEmpl(a.id,a.jobNumber,a.userName,"+
			 "a.startTime,a.endTime,a.nationality,"+
			 "a.maritalStatus,a.gender,a.education,"+
			 "a.identificationNumber,a.birthday,a.age,"+
			 "c.name,b.groupName,a.station,a.structure,"+
			 "a.residence,a.currentAddress,a.contactNumber,"+
			 "a.inDate,a.ageOfWork,a.probationPeriod,"+
			 "a.departureDate,a.socialSecurityNumber,"+
			 "a.providentFundAccount,a.remarks,a.poStartDate,"+
			 "a.poEndDate,a.supplementalAgreement,"+
			 "a.identityCardStart,a.identityCardEnd,a.major,"+
			 "a.graduateFrom,a.studyStart,a.studyEnd,"+
			 "a.schoolModel,a.certificateNumber,a.studyForm,"+
			 "a.credential,a.travellingCrane,a.forklift,"+
			 "a.electricSoldering,a.safetyOfficer,"+
			 "a.electricianCertificate,a.workLocation, a.month,"+
			 "a.retireDate,a.line) from Employee a,Group b,Department c where a.groupId=b.id and a.depId=c.id and a.userName like %?1%")
	public List<GroupVoEmpl> findempls(String userName);
}
