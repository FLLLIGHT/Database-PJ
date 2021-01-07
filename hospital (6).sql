-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- 主机： localhost
-- 生成日期： 2021-01-07 17:29:17
-- 服务器版本： 10.4.13-MariaDB
-- PHP 版本： 7.4.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `hospital`
--

-- --------------------------------------------------------

--
-- 表的结构 `area`
--

CREATE TABLE `area` (
  `areaId` int(11) NOT NULL,
  `type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `area`
--

INSERT INTO `area` (`areaId`, `type`) VALUES
(1, 'mild case area'),
(2, 'severe condition area'),
(3, 'critical condition area'),
(4, 'isolated area'),
(5, 'out of hospital');

-- --------------------------------------------------------

--
-- 表的结构 `bed`
--

CREATE TABLE `bed` (
  `bedId` int(11) NOT NULL,
  `patientId` varchar(128) DEFAULT NULL,
  `roomId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `bed`
--

INSERT INTO `bed` (`bedId`, `patientId`, `roomId`) VALUES
(1, '', 1),
(2, '', 2),
(3, '', 3);

--
-- 触发器 `bed`
--
DELIMITER $$
CREATE TRIGGER `bed_limit` BEFORE INSERT ON `bed` FOR EACH ROW BEGIN
  IF ((select count(bedId) from bed where roomId = NEW.roomId) >= 4) and (1 in (SELECT areaId from room where roomId = NEW.roomId))
  THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'MAX BED IN MILD';
  END IF;
  IF ((select count(bedId) from bed where roomId = NEW.roomId) >= 2) and (2 in (SELECT areaId from room where roomId = NEW.roomId))
  THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'MAX BED IN SEVERE';
  END IF;
  IF ((select count(bedId) from bed where roomId = NEW.roomId) >= 1) and (3 in (SELECT areaId from room where roomId = NEW.roomId))
  THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'MAX BED IN CRITICAL';
  END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- 表的结构 `chief_nurse`
--

CREATE TABLE `chief_nurse` (
  `chiefNurseId` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `areaId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `chief_nurse`
--

INSERT INTO `chief_nurse` (`chiefNurseId`, `name`, `password`, `areaId`) VALUES
(1, 'Dale', '123456', 1),
(2, 'Eric', '123456', 2),
(3, 'Frank', '123456', 3);

-- --------------------------------------------------------

--
-- 表的结构 `daily_record`
--

CREATE TABLE `daily_record` (
  `recordId` int(11) NOT NULL,
  `patientId` varchar(511) NOT NULL,
  `temperature` float NOT NULL,
  `symptom` varchar(255) DEFAULT NULL,
  `lifeStatus` int(11) NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `doctor`
--

CREATE TABLE `doctor` (
  `doctorId` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `areaId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `doctor`
--

INSERT INTO `doctor` (`doctorId`, `name`, `password`, `areaId`) VALUES
(1, 'Alice', '123456', 1),
(2, 'Bob', '123456', 2),
(3, 'Cindy', '123456', 3);

-- --------------------------------------------------------

--
-- 表的结构 `emergency_nurse`
--

CREATE TABLE `emergency_nurse` (
  `emergencyNurseId` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `emergency_nurse`
--

INSERT INTO `emergency_nurse` (`emergencyNurseId`, `name`, `password`) VALUES
(1, 'Tom', '123456');

-- --------------------------------------------------------

--
-- 表的结构 `illness_evaluation`
--

CREATE TABLE `illness_evaluation` (
  `evaluationId` int(11) NOT NULL,
  `evaluation` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `illness_evaluation`
--

INSERT INTO `illness_evaluation` (`evaluationId`, `evaluation`) VALUES
(1, 'mild case'),
(2, 'severe condition'),
(3, 'critical condition');

-- --------------------------------------------------------

--
-- 表的结构 `life_status`
--

CREATE TABLE `life_status` (
  `lifeStatusId` int(11) NOT NULL,
  `lifeStatus` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `life_status`
--

INSERT INTO `life_status` (`lifeStatusId`, `lifeStatus`) VALUES
(1, 'healed'),
(2, 'in hospital'),
(3, 'dead');

-- --------------------------------------------------------

--
-- 表的结构 `message`
--

CREATE TABLE `message` (
  `messageId` int(11) NOT NULL,
  `toId` int(11) NOT NULL,
  `toType` int(11) NOT NULL,
  `messageContent` varchar(1024) NOT NULL,
  `alreadyRead` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `patient`
--

CREATE TABLE `patient` (
  `patientId` varchar(128) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(2047) NOT NULL,
  `gender` varchar(63) NOT NULL,
  `telephone` varchar(63) NOT NULL,
  `areaId` int(11) NOT NULL,
  `evaluation` int(11) NOT NULL,
  `lifeStatus` int(11) NOT NULL,
  `nurseId` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `room`
--

CREATE TABLE `room` (
  `roomId` int(11) NOT NULL,
  `areaId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `room`
--

INSERT INTO `room` (`roomId`, `areaId`) VALUES
(1, 1),
(2, 2),
(3, 3);

-- --------------------------------------------------------

--
-- 表的结构 `test_result`
--

CREATE TABLE `test_result` (
  `testResultId` int(11) NOT NULL,
  `patientId` varchar(128) NOT NULL,
  `date` datetime NOT NULL,
  `testResult` varchar(255) NOT NULL,
  `evaluation` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `ward_nurse`
--

CREATE TABLE `ward_nurse` (
  `wardNurseId` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `areaId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `ward_nurse`
--

INSERT INTO `ward_nurse` (`wardNurseId`, `name`, `password`, `areaId`) VALUES
(1, 'A', '12345', 1),
(2, 'Helen', '123456', 2);

--
-- 转储表的索引
--

--
-- 表的索引 `area`
--
ALTER TABLE `area`
  ADD PRIMARY KEY (`areaId`);

--
-- 表的索引 `bed`
--
ALTER TABLE `bed`
  ADD PRIMARY KEY (`bedId`),
  ADD KEY `room_bed` (`roomId`);

--
-- 表的索引 `chief_nurse`
--
ALTER TABLE `chief_nurse`
  ADD PRIMARY KEY (`chiefNurseId`),
  ADD KEY `chief_nurse_area` (`areaId`);

--
-- 表的索引 `daily_record`
--
ALTER TABLE `daily_record`
  ADD PRIMARY KEY (`recordId`),
  ADD KEY `life_status_record` (`lifeStatus`),
  ADD KEY `patient_daily_record` (`patientId`);

--
-- 表的索引 `doctor`
--
ALTER TABLE `doctor`
  ADD PRIMARY KEY (`doctorId`),
  ADD KEY `doctor_area` (`areaId`);

--
-- 表的索引 `emergency_nurse`
--
ALTER TABLE `emergency_nurse`
  ADD PRIMARY KEY (`emergencyNurseId`);

--
-- 表的索引 `illness_evaluation`
--
ALTER TABLE `illness_evaluation`
  ADD PRIMARY KEY (`evaluationId`);

--
-- 表的索引 `life_status`
--
ALTER TABLE `life_status`
  ADD PRIMARY KEY (`lifeStatusId`);

--
-- 表的索引 `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`messageId`);

--
-- 表的索引 `patient`
--
ALTER TABLE `patient`
  ADD PRIMARY KEY (`patientId`),
  ADD KEY `evaluation` (`evaluation`),
  ADD KEY `area` (`areaId`),
  ADD KEY `life_status` (`lifeStatus`);

--
-- 表的索引 `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`roomId`),
  ADD KEY `area_room` (`areaId`);

--
-- 表的索引 `test_result`
--
ALTER TABLE `test_result`
  ADD PRIMARY KEY (`testResultId`),
  ADD KEY `evaluation_test` (`evaluation`),
  ADD KEY `patient_test` (`patientId`);

--
-- 表的索引 `ward_nurse`
--
ALTER TABLE `ward_nurse`
  ADD PRIMARY KEY (`wardNurseId`),
  ADD KEY `ward_nurse_area` (`areaId`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `area`
--
ALTER TABLE `area`
  MODIFY `areaId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用表AUTO_INCREMENT `bed`
--
ALTER TABLE `bed`
  MODIFY `bedId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- 使用表AUTO_INCREMENT `chief_nurse`
--
ALTER TABLE `chief_nurse`
  MODIFY `chiefNurseId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- 使用表AUTO_INCREMENT `daily_record`
--
ALTER TABLE `daily_record`
  MODIFY `recordId` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `doctor`
--
ALTER TABLE `doctor`
  MODIFY `doctorId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- 使用表AUTO_INCREMENT `illness_evaluation`
--
ALTER TABLE `illness_evaluation`
  MODIFY `evaluationId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用表AUTO_INCREMENT `life_status`
--
ALTER TABLE `life_status`
  MODIFY `lifeStatusId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用表AUTO_INCREMENT `message`
--
ALTER TABLE `message`
  MODIFY `messageId` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `room`
--
ALTER TABLE `room`
  MODIFY `roomId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用表AUTO_INCREMENT `test_result`
--
ALTER TABLE `test_result`
  MODIFY `testResultId` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `ward_nurse`
--
ALTER TABLE `ward_nurse`
  MODIFY `wardNurseId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 限制导出的表
--

--
-- 限制表 `bed`
--
ALTER TABLE `bed`
  ADD CONSTRAINT `room_bed` FOREIGN KEY (`roomId`) REFERENCES `room` (`roomId`);

--
-- 限制表 `chief_nurse`
--
ALTER TABLE `chief_nurse`
  ADD CONSTRAINT `chief_nurse_area` FOREIGN KEY (`areaId`) REFERENCES `area` (`areaId`);

--
-- 限制表 `daily_record`
--
ALTER TABLE `daily_record`
  ADD CONSTRAINT `life_status_record` FOREIGN KEY (`lifeStatus`) REFERENCES `life_status` (`lifeStatusId`),
  ADD CONSTRAINT `patient_daily_record` FOREIGN KEY (`patientId`) REFERENCES `patient` (`patientId`);

--
-- 限制表 `doctor`
--
ALTER TABLE `doctor`
  ADD CONSTRAINT `doctor_area` FOREIGN KEY (`areaId`) REFERENCES `area` (`areaId`);

--
-- 限制表 `patient`
--
ALTER TABLE `patient`
  ADD CONSTRAINT `area` FOREIGN KEY (`areaId`) REFERENCES `area` (`areaId`),
  ADD CONSTRAINT `evaluation` FOREIGN KEY (`evaluation`) REFERENCES `illness_evaluation` (`evaluationId`),
  ADD CONSTRAINT `life_status` FOREIGN KEY (`lifeStatus`) REFERENCES `life_status` (`lifeStatusId`);

--
-- 限制表 `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `area_room` FOREIGN KEY (`areaId`) REFERENCES `area` (`areaId`);

--
-- 限制表 `test_result`
--
ALTER TABLE `test_result`
  ADD CONSTRAINT `evaluation_test` FOREIGN KEY (`evaluation`) REFERENCES `illness_evaluation` (`evaluationId`),
  ADD CONSTRAINT `patient_test` FOREIGN KEY (`patientId`) REFERENCES `patient` (`patientId`);

--
-- 限制表 `ward_nurse`
--
ALTER TABLE `ward_nurse`
  ADD CONSTRAINT `ward_nurse_area` FOREIGN KEY (`areaId`) REFERENCES `area` (`areaId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
