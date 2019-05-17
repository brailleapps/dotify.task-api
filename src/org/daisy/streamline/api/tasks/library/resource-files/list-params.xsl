<?xml version="1.0" encoding="UTF-8"?>
<!-- 
		Lists xslt parameters that have a description attribute (@xtd:desc).
		Roughly respects xslt include/import precedence.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xtd="https://www.ologolo.org/ns/doc/xsl"
	exclude-result-prefixes="xsl xtd"
	version="2.0">
	<xsl:output indent="yes"  doctype-system="http://java.sun.com/dtd/properties.dtd" standalone="no"/>

	<xsl:template match="/">
		<properties>
			<xsl:variable name="list">
				<xsl:apply-templates/>
			</xsl:variable>
			<xsl:for-each select="distinct-values($list/*/@key)">
				<xsl:variable name="key" select="."/>
				<xsl:copy-of select="$list/*[@key=$key][last()]"/>
			</xsl:for-each>
		</properties>
	</xsl:template>
	<xsl:template match="/*">
		<!-- The root element -->
		<xsl:apply-templates select="xsl:import"/>
		<xsl:apply-templates select="xsl:include"/>
		<xsl:apply-templates select="xsl:param"/>
	</xsl:template>
	<xsl:template match="xsl:import|xsl:include">
		<xsl:apply-templates select="document(@href)/node()"/>
	</xsl:template>
	<xsl:template match="xsl:param">
		<xsl:if test="@xtd:desc">
			<!-- using tab as field separator, any tabs inside values will be converted to a regular space -->
			<entry key="{@name}"><xsl:value-of select="concat(normalize-space(@xtd:default), '&#0009;', normalize-space(@xtd:values), '&#0009;', normalize-space(@xtd:desc))"/></entry>
		</xsl:if>
	</xsl:template>
	<xsl:template match="node()">
		<xsl:apply-templates/>
	</xsl:template>
</xsl:stylesheet>