-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 31, 2019 lúc 08:27 AM
-- Phiên bản máy phục vụ: 10.1.38-MariaDB
-- Phiên bản PHP: 7.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `chat`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `message`
--

CREATE TABLE `message` (
  `User1` varchar(20) NOT NULL,
  `User2` varchar(20) NOT NULL,
  `Content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_vietnamese_ci NOT NULL,
  `Time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `message`
--

INSERT INTO `message` (`User1`, `User2`, `Content`, `Time`) VALUES
('a', 'khanh', 'Đây là đoạn chat đầu tiên', '2019-05-26 08:04:13'),
('a', 'khanh', 'Hello', '2019-05-26 09:04:13'),
('khanh', 'a', 'hi', '2019-05-26 09:04:20'),
('a', 'khanh', 'sdcd', '2019-05-26 09:04:30'),
('a', 'khanh', 'alo', '2019-05-27 14:25:41'),
('khanh', 'a', 'blo', '2019-05-27 14:32:09'),
('a', 'khanh', 'abc', '2019-05-27 14:40:24'),
('a', 'khanh', 'what', '2019-05-27 14:46:06'),
('khanh', 'a', 'what j', '2019-05-27 14:46:27'),
('a', 'khanh', '?ây là ?o?n test ti?ng vi?t', '2019-05-27 14:53:16'),
('a', 'khanh', 'l?i j nh?', '2019-05-27 14:53:51'),
('a', 'khanh', 't?i sao không ch?y ?c ti?ng vi?t nh?', '2019-05-27 14:59:18'),
('a', 'khanh', 'tại sao không chạy tiếng việt', '2019-05-27 14:59:42'),
('a', 'khanh', 'viết đc tiếng việt chưa nhỉ', '2019-05-27 15:10:30'),
('khanh', 'a', 'ồ đc rồi nè', '2019-05-27 15:11:02'),
('b', 'a', 'abc', '2019-05-27 15:25:31'),
('b', 'a', 'abc', '2019-05-27 16:04:09'),
('b', 'a', 'abc', '2019-05-27 16:23:18'),
('b', 'a', 'a', '2019-05-27 16:26:35'),
('b', 'a', 'bcd', '2019-05-27 16:28:22'),
('b', 'a', 'test thong baos', '2019-05-27 16:30:48'),
('b', 'a', 'test thông báo', '2019-05-27 16:43:16'),
('b', 'a', 'test thông báo', '2019-05-27 16:43:47'),
('b', '', 'test thông báo lần 3', '2019-05-27 16:58:12'),
('b', 'a', 'test thông báo lần cuối', '2019-05-27 17:01:18'),
('a', 'b', 'vẫn chưa đc ', '2019-05-27 17:03:58'),
('b', 'a', 'thử lại lần cuối nè', '2019-05-27 17:07:15'),
('a', 'b', 'vẫn chưa được', '2019-05-27 17:07:55'),
('b', 'a', 'thử lại xem', '2019-05-27 21:46:31'),
('b', 'a', 'helo a', '2019-05-27 23:50:24'),
('a', 'b', 'helo b', '2019-05-27 23:50:54'),
('b', 'a', 'test thoong baos tiep', '2019-05-28 00:25:59'),
('a', 'b', 'vẫn không được', '2019-05-28 00:26:25'),
('b', 'a', 'Tại sao không được', '2019-05-28 00:29:21'),
('a', 'b', 'chịu rồi', '2019-05-28 00:32:33'),
('b', 'a', 'ht hì đc chưa', '2019-05-28 00:34:54'),
('a', 'b', 'ok rồi', '2019-05-28 00:35:12'),
('b', 'a', 'quá ok', '2019-05-28 00:35:30'),
('b', 'a', 'vẫn chưa ok', '2019-05-28 00:43:52'),
('a', 'b', 'thử lại nào', '2019-05-28 00:49:03'),
('a', 'b', 'đc chưa', '2019-05-28 00:49:41'),
('b', 'a', 'vẫn chưa đc', '2019-05-28 00:52:04'),
('a', 'b', 'tiếp nào', '2019-05-28 00:53:11'),
('a', 'b', 'ok chưa', '2019-05-28 00:53:35'),
('b', 'a', 'quá ok', '2019-05-28 00:53:41'),
('a', 'b', 'chat coi nào', '2019-05-28 01:00:26'),
('b', 'a', 'cps', '2019-05-28 01:00:34'),
('a', 'b', 'ok', '2019-05-28 01:00:39'),
('a', 'b', 'ngon rồi', '2019-05-28 01:00:45'),
('b', 'a', 'việt', '2019-05-28 01:00:46'),
('b', 'a', 'rep', '2019-05-28 01:01:14'),
('b', 'c', 'hello', '2019-05-28 01:01:41'),
('b', 'c', 'lô', '2019-05-28 01:01:52'),
('a', 'khanh', 'đc rồi à', '2019-05-28 01:14:24'),
('b', 'a', 'bcd', '2019-05-29 12:11:32'),
('a', 'b', '??', '2019-05-29 12:12:00'),
('b', 'a', 'sao k nghe máy', '2019-05-29 20:56:30'),
('b', 'a', 'trả lời coi', '2019-05-29 21:06:07'),
('a', 'b', 'k thích đấy', '2019-05-29 21:06:32'),
('b', 'a', 'láo', '2019-05-29 21:06:47'),
('a', 'b', 'láo ư ?', '2019-05-29 21:35:23'),
('khanh', 'phuong', 'alo phương', '2019-05-30 01:11:11'),
('khanh', 'phuong', 'alo', '2019-05-30 01:11:23'),
('phuong', 'khanh', 'alo', '2019-05-30 01:12:48'),
('phuong', 'test', 'chào test nha', '2019-05-30 01:14:49'),
('phuong', 'khanh', 'alo', '2019-05-30 01:16:18'),
('a', 'khanh', 'abc', '2019-05-30 12:33:14'),
('b', 'a', 'alo', '2019-05-30 20:14:03'),
('b', 'a', 'alo', '2019-05-30 20:15:11'),
('a', 'b', '??', '2019-05-30 20:23:06'),
('a', 'b', '??', '2019-05-30 20:23:16'),
('b', 'a', 'qdasdas', '2019-05-31 00:04:46'),
('a', 'b', 'alo nhân', '2019-05-31 00:04:52'),
('b', 'a', 'asdasd', '2019-05-31 00:04:55'),
('a', 'b', 'some thing', '2019-05-31 08:48:23'),
('a', 'b', 'abc', '2019-05-31 08:48:40'),
('a', 'd', 'gg', '2019-05-31 08:48:54');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `Username` varchar(20) NOT NULL,
  `Password` varchar(20) NOT NULL,
  `Status` int(11) NOT NULL DEFAULT '0',
  `Image` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`Username`, `Password`, `Status`, `Image`) VALUES
('a', '1', 0, '/img/boy'),
('b', '1', 0, '/img/man'),
('c', '1', 0, '/img/girl'),
('d', '1', 1, '/img/woman'),
('khanh', '1', 0, '/img/boy'),
('phuong', '1', 0, '/img/woman'),
('test', '2', 0, '/img/girl');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`Username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
