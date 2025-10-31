-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-10-2025 a las 16:33:32
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `aither`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `credenciales`
--

CREATE TABLE `credenciales` (
  `id` int(11) NOT NULL,
  `credencial` text NOT NULL,
  `permisos` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `credenciales`
--

INSERT INTO `credenciales` (`id`, `credencial`, `permisos`) VALUES
(1, 'General', 'Acceso básico a sensores y perfil personal'),
(2, 'Técnico', 'Gestión de incidencias y soporte de sensores'),
(3, 'Administrador', 'Acceso completo a todos los módulos del sistema');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `fotos_incidencia`
--

CREATE TABLE `fotos_incidencia` (
  `id` int(11) NOT NULL,
  `incidencia_id` int(11) NOT NULL,
  `foto` mediumblob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `incidencias`
--

CREATE TABLE `incidencias` (
  `id` int(11) NOT NULL,
  `id_tecnico` int(11) DEFAULT NULL,
  `id_user` int(11) DEFAULT NULL,
  `titulo` varchar(255) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_finalizacion` timestamp NULL DEFAULT NULL,
  `activa` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `medicion`
--

CREATE TABLE `medicion` (
  `id` int(11) NOT NULL,
  `tipo_medicion_id` int(11) NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `hora` timestamp NOT NULL DEFAULT current_timestamp(),
  `localizacion` varchar(255) DEFAULT NULL,
  `sensor_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sensor`
--

CREATE TABLE `sensor` (
  `id` int(11) NOT NULL,
  `mac` varchar(50) NOT NULL,
  `problema` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_medicion`
--

CREATE TABLE `tipo_medicion` (
  `id` int(11) NOT NULL,
  `medida` varchar(100) NOT NULL,
  `unidad` varchar(50) NOT NULL,
  `txt` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(150) DEFAULT NULL,
  `gmail` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `credencial_id` int(11) DEFAULT NULL,
  `biometria` mediumblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `nombre`, `apellidos`, `gmail`, `password`, `credencial_id`, `biometria`) VALUES
(1, 'Manuel', 'Pérez Garcia', 'mpergar9@upv.edu', 'pablothegoat', 3, NULL),
(2, 'Greycy', 'Burgos Salazar', 'grey@gmail.com', 'asdfghjkl', 2, NULL),
(3, 'Pablo', 'BoxMark', 'palomaperu@gmail.com', 'qwertyuiop', 1, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_sensor`
--

CREATE TABLE `usuario_sensor` (
  `id_relacion` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `sensor_id` int(11) NOT NULL,
  `actual` tinyint(1) DEFAULT 1,
  `inicio_relacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fin_relacion` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `credenciales`
--
ALTER TABLE `credenciales`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `fotos_incidencia`
--
ALTER TABLE `fotos_incidencia`
  ADD PRIMARY KEY (`id`),
  ADD KEY `incidencia_id` (`incidencia_id`);

--
-- Indices de la tabla `incidencias`
--
ALTER TABLE `incidencias`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_incidencias_activa` (`activa`),
  ADD KEY `idx_incidencias_usuarios` (`id_tecnico`,`id_user`),
  ADD KEY `fk_incidencias_user` (`id_user`);

--
-- Indices de la tabla `medicion`
--
ALTER TABLE `medicion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tipo_medicion_id` (`tipo_medicion_id`),
  ADD KEY `idx_medicion_sensor_hora` (`sensor_id`,`hora`);

--
-- Indices de la tabla `sensor`
--
ALTER TABLE `sensor`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `mac` (`mac`);

--
-- Indices de la tabla `tipo_medicion`
--
ALTER TABLE `tipo_medicion`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `gmail` (`gmail`),
  ADD KEY `credencial_id` (`credencial_id`);

--
-- Indices de la tabla `usuario_sensor`
--
ALTER TABLE `usuario_sensor`
  ADD PRIMARY KEY (`id_relacion`),
  ADD KEY `sensor_id` (`sensor_id`),
  ADD KEY `idx_usuario_sensor_actual` (`usuario_id`,`actual`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `credenciales`
--
ALTER TABLE `credenciales`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `fotos_incidencia`
--
ALTER TABLE `fotos_incidencia`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `incidencias`
--
ALTER TABLE `incidencias`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `medicion`
--
ALTER TABLE `medicion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `sensor`
--
ALTER TABLE `sensor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tipo_medicion`
--
ALTER TABLE `tipo_medicion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `usuario_sensor`
--
ALTER TABLE `usuario_sensor`
  MODIFY `id_relacion` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `fotos_incidencia`
--
ALTER TABLE `fotos_incidencia`
  ADD CONSTRAINT `fotos_incidencia_ibfk_1` FOREIGN KEY (`incidencia_id`) REFERENCES `incidencias` (`id`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `incidencias`
--
ALTER TABLE `incidencias`
  ADD CONSTRAINT `fk_incidencias_tecnico` FOREIGN KEY (`id_tecnico`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_incidencias_user` FOREIGN KEY (`id_user`) REFERENCES `usuario` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Filtros para la tabla `medicion`
--
ALTER TABLE `medicion`
  ADD CONSTRAINT `medicion_ibfk_1` FOREIGN KEY (`tipo_medicion_id`) REFERENCES `tipo_medicion` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `medicion_ibfk_2` FOREIGN KEY (`sensor_id`) REFERENCES `sensor` (`id`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`credencial_id`) REFERENCES `credenciales` (`id`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `usuario_sensor`
--
ALTER TABLE `usuario_sensor`
  ADD CONSTRAINT `usuario_sensor_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `usuario_sensor_ibfk_2` FOREIGN KEY (`sensor_id`) REFERENCES `sensor` (`id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
