<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright (C) 2002 Albert Tumanov

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

-->
<!--$Header: /cvsroot/pldoc/sources/src/resources/summary.xsl,v 1.2 2005/01/14 10:16:27 t_schaedler Exp $-->

<!DOCTYPE xsl:stylesheet [
<!ENTITY nbsp "&#160;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>

  <xsl:variable name="uppercase">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>
  <xsl:variable name="lowercase">abcdefghijklmnopqrstuvwxyz</xsl:variable>

  <xsl:key name="schemaInit" match="*[@SCHEMA]" use="@SCHEMA"/>

  <!-- ********************** NAVIGATION BAR TEMPLATE ********************** -->
  <xsl:template name="NavigationBar">
    <TABLE BORDER="0" WIDTH="100%" CELLPADDING="1" CELLSPACING="0">
    <TR>
    <TD COLSPAN="2" CLASS="NavBarRow1">
    <TABLE BORDER="0" CELLPADDING="0" CELLSPACING="3">
      <TR ALIGN="center" VALIGN="top">
      <TD CLASS="NavBarRow1Chosen"><FONT CLASS="NavBarFont1Chosen"><B>Overview</B></FONT> &nbsp;</TD>
      <TD CLASS="NavBarRow1"><A HREF="deprecated-list.html"><FONT CLASS="NavBarFont1"><B>Deprecated</B></FONT></A> &nbsp;</TD>
      <TD CLASS="NavBarRow1"><A HREF="index-list.html"><FONT CLASS="NavBarFont1"><B>Index</B></FONT></A> &nbsp;</TD>
      <TD CLASS="NavBarRow1"><A HREF="generator.html"><FONT CLASS="NavBarFont1"><B>Generator</B></FONT></A> &nbsp;</TD>
      </TR>
    </TABLE>
    </TD>
    <TD ALIGN="right" VALIGN="top" rowspan="3"><EM>
      <b><xsl:value-of select="@NAME"/></b></EM>
    </TD>
    </TR>

    </TABLE>
    <HR/>
  </xsl:template>

  <!-- ************************* START OF PAGE ***************************** -->
  <xsl:template match="/APPLICATION">
    <HTML>
    <HEAD>
    <TITLE><xsl:value-of select="@NAME" />: Overview</TITLE>
    <LINK REL ="stylesheet" TYPE="text/css" HREF="stylesheet.css" TITLE="Style" />
    </HEAD>

    <BODY BGCOLOR="white">
    <!-- **************************** HEADER ******************************* -->
    <xsl:call-template name="NavigationBar"/>

    <CENTER><H2><xsl:value-of select="@NAME" /></H2></CENTER>
    <xsl:value-of select="OVERVIEW" disable-output-escaping="yes" />
    <P/><P/>

    <!-- **************************** SCHEMAS ******************************* -->
		<xsl:if test="@SCHEMA">
		
			<TABLE BORDER="1" WIDTH="100%">
			<TR><TD COLSPAN="2"><FONT size="+1" CLASS="FrameHeadingFont">Schemas</FONT></TD></TR>
	
	    <!-- list all distinct schemas using muenchian method and adds a link to its summary -->
			<xsl:for-each select="//*[count(. | key('schemaInit', @SCHEMA)[1]) = 1 and @SCHEMA != '']">
			  <TR>
	   		  <TD>
	      	  <FONT CLASS="FrameItemFont"><A HREF="{translate(@SCHEMA, $uppercase, $lowercase)}-summary.html" TARGET="packageFrame">
	        	   <xsl:value-of select="translate(@SCHEMA, $lowercase, $uppercase)"/>
		        </A></FONT>
			    </TD>
		  	  <TD>&nbsp;</TD>
			  </TR>
			</xsl:for-each>
			</TABLE>
			
		</xsl:if>

	<P/><P/>
	
    <!-- ***************************** FOOTER ****************************** -->
    <xsl:call-template name="NavigationBar"/>

		<FONT size="-1">
			<TABLE width="100%">
				<TR>
					<TD align="left">
	    			Generated by <A HREF="http://pldoc.sourceforge.net" TARGET="_blank">PLDoc</A>
	    		</TD>
	    		<TD align="right">
	    		created 
	    		<xsl:value-of select="GENERATOR/CREATED/@DATE"/>&nbsp;-&nbsp;<xsl:value-of select="GENERATOR/CREATED/@TIME"/>
	    		</TD>
	    	</TR>
	    </TABLE>
		</FONT>

    </BODY>
    </HTML>
  </xsl:template>

</xsl:stylesheet>
