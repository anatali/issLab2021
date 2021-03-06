<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="concept">
   <html>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<meta name="copyright" content="(C) Copyright 2005" />
<meta name="DC.rights.owner" content="(C) Copyright 2005" />
<meta content="concept" name="DC.Type" />
<meta name="DC.Title" content="Bibliografia" />
<meta content="XHTML" name="DC.Format" />
<meta content="concept" name="DC.Identifier" />
<meta content="en-us" name="DC.Language" />
<link href="../css/commonltr.css" type="text/css" rel="stylesheet" />
<title>Bibliografia</title>
</head>
      <body>
         <xsl:apply-templates select="title"/>
         <xsl:apply-templates select="conbody"/>
      </body>
   </html>
</xsl:template>

<xsl:template match="conbody">
<body>
	<xsl:apply-templates/>
</body>
</xsl:template>

<xsl:template match="section">
   <xsl:apply-templates />
</xsl:template>


<xsl:template match="title">
   <h1 class="topictitle1"><xsl:apply-templates /></h1>
</xsl:template>

<!---->
<xsl:template match="xref">
<xsl:if test="@href">
   <a href="{@href}"><xsl:apply-templates/></a>
</xsl:if>
<xsl:if test="@name">
   <a href="{@name}"><xsl:apply-templates/></a>
</xsl:if>
</xsl:template>

<xsl:template match="ul">
   <ul><xsl:apply-templates/></ul>
</xsl:template>

<xsl:template match="ol">
   <ol><xsl:apply-templates/></ol>
</xsl:template>

<xsl:template match="li">
   <li><xsl:apply-templates/></li>
</xsl:template>

<xsl:template match="br">
   <br/>
</xsl:template>

<xsl:template match="b">
   <b><xsl:apply-templates/></b>
</xsl:template>

<xsl:template match="h1">
   <h1><xsl:apply-templates/></h1>
</xsl:template>

<xsl:template match="h2">
   <h2><xsl:apply-templates/></h2>
</xsl:template>

<xsl:template match="h3">
   <h3><xsl:apply-templates/></h3>
</xsl:template>

<xsl:template match="h4">
   <h4><xsl:apply-templates/></h4>
</xsl:template>

<xsl:template match="h5">
   <h5><xsl:apply-templates/></h5>
</xsl:template>

<xsl:template match="em">
   <em><xsl:apply-templates/></em>
</xsl:template>

<xsl:template match="dl">
   <dl><xsl:apply-templates/></dl>
</xsl:template>

<xsl:template match="dt">
   <dt><xsl:apply-templates/></dt>
</xsl:template>

<xsl:template match="dd">
   <dd><xsl:apply-templates/></dd>
</xsl:template>

<xsl:template match="center">
   <center><xsl:apply-templates/></center>
</xsl:template>

<xsl:template match="tt | ttt">
   <tt><xsl:apply-templates/></tt>
</xsl:template>

<xsl:template match="pre">
   <pre><xsl:apply-templates/></pre>
</xsl:template>

<xsl:template match="i">
   <i><xsl:apply-templates/></i>
</xsl:template>

<xsl:template match="p">
   <p >
   <xsl:apply-templates/>
   </p>
</xsl:template>


<xsl:template match="table">
   <table class="bodyTable"><xsl:apply-templates /></table>
</xsl:template>

<xsl:template match="thead">
   <thead><tr class="b"><xsl:apply-templates/></tr></thead>
</xsl:template>

<xsl:template match="th">
   <th width="{@width}"><xsl:apply-templates/></th>
</xsl:template>

<xsl:template match="tr">
   <tr class="a" width="{@width}"><xsl:apply-templates/></tr>
</xsl:template>

<xsl:template match="td">
   <td align="{@align}" rowspan="{@rowspan}"><xsl:apply-templates /></td>
 </xsl:template>

<xsl:template match="hr">
   <hr/>
</xsl:template>

<xsl:template match="detail">
   <hr/>
   	<tt><xsl:value-of select="@name"/></tt><br/>
   	<xsl:apply-templates/>
   <hr/>
</xsl:template>

 
<xsl:template match="image">
    <hr/>
    <xsl:if test="@width">
   	<img src="{@href}" width="{@width}" height="{@height}"/>
   </xsl:if>
    <xsl:if test="not(@width)">
   	<img src="{@href}" />
   </xsl:if>

   <hr/>
</xsl:template>


<xsl:template match ="cite | CITE">
[<cite keyref="{@keyref}">  </cite>]
</xsl:template>

<xsl:template match ="blockquote">
	<blockquote><xsl:apply-templates/></blockquote>
</xsl:template>

<xsl:template match ="script">
<p>loading the script file <xsl:value-of select="@src"/> </p>
	<script language="JavaScript1.2" SRC="{@src}"></script> 
</xsl:template>

<xsl:template match ="applet">
	<applet width="{@width}" height="{@height}"  name="{@name}"
	code="{@code}" codebase="{@codebase}"
	archive="{@archive}">
	<xsl:apply-templates/></applet>
</xsl:template>

<xsl:template match="source">
<blockquote>
<hr/>
<pre><xsl:value-of select="@name"/> <br/>
   <xsl:apply-templates/></pre>
<hr/>
</blockquote>
</xsl:template>

<xsl:template match ="decoi">
	<br/><font color="red"><xsl:apply-templates/></font>
</xsl:template>

<xsl:template match ="sub | SUB"><sub><xsl:apply-templates/></sub></xsl:template>
<xsl:template match ="sup | SUP"><sup><xsl:apply-templates/></sup></xsl:template>
<xsl:template match ="ge | GE"><font face="symbol">­³</font></xsl:template>
<xsl:template match ="le | LE"><font face="symbol">£</font></xsl:template>
<xsl:template match ="ge">>=</xsl:template>
<xsl:template match ="ll">&lt;&lt;</xsl:template>
<xsl:template match ="gg">>></xsl:template>
<xsl:template match ="cup | CUP"><font face="symbol">È</font></xsl:template> <!-- U -->
<xsl:template match ="cap | CAP"><font face="symbol">Ç</font></xsl:template> <!--  -->
<xsl:template match ="in | IN"> <font face="symbol">Î</font></xsl:template>
<xsl:template match ="subseteq"> <font face="symbol">Í</font></xsl:template>
<xsl:template match ="supseteq"> <font face="symbol">Ê</font></xsl:template>
<xsl:template match ="equiv"> <font face="symbol">º</font></xsl:template>
<xsl:template match ="approx"> <font face="symbol">»</font></xsl:template>
<xsl:template match ="neq"> <font face="symbol">¹</font></xsl:template>
<xsl:template match ="notin"> <font face="symbol">Ï</font></xsl:template>
<xsl:template match ="sim"><font face="symbol">~</font></xsl:template> <!--tilde-->
<xsl:template match ="simeq"><font face="symbol"><u>~</u></font></xsl:template>
<xsl:template match ="cxong"><font face="symbol">@</font></xsl:template>
<xsl:template match ="vdash"><font face="symbol">½-</font></xsl:template>
<xsl:template match ="models"><font face="symbol">½=</font></xsl:template>
<xsl:template match ="rightarrow">-&gt;</xsl:template> 
<xsl:template match ="Rightarrow">=&gt;</xsl:template> 
<xsl:template match ="leftarrow">&lt;-</xsl:template> 
<xsl:template match ="Leftarrow">&lt;=</xsl:template> 
<xsl:template match ="ldots"> ... </xsl:template> 
<xsl:template match ="models">|=</xsl:template>

<xsl:template match ="E"><font face="symbol">$</font></xsl:template> 
<xsl:template match ="V"><font face="symbol">"</font></xsl:template> 

<xsl:template match ="alpha"><font face="symbol">a</font></xsl:template>
<xsl:template match ="beta"><font face="symbol">b</font></xsl:template>
<xsl:template match ="gamma"><font face="symbol">c</font></xsl:template>
<xsl:template match ="delta"><font face="symbol">d</font></xsl:template>
<xsl:template match ="epsilon"><font face="symbol">e</font></xsl:template>
<xsl:template match ="phi"><font face="symbol">Æ</font></xsl:template> <!-- vuoto  -->
<xsl:template match ="eta"><font face="symbol">eta</font></xsl:template> <!-- vuoto  -->
<xsl:template match ="dot"><font face="symbol">·</font></xsl:template> 

<!--
-->

</xsl:stylesheet>