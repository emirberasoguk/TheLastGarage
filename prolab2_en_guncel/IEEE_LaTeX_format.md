%% Lab Report for EEET2493_labreport_template.tex
%% V1.0
%% 2019/01/16
%% This is the template for a Lab report following an IEEE paper. Modified by Francisco Tovar after Michael Sheel original document.


%% This is a skeleton file demonstrating the use of IEEEtran.cls
%% (requires IEEEtran.cls version 1.8b or later) with an IEEE
%% journal paper.
%%
%% Support sites:
%% http://www.michaelshell.org/tex/ieeetran/
%% http://www.ctan.org/pkg/ieeetran
%% and
%% http://www.ieee.org/

%%*************************************************************************
%% Legal Notice:
%% This code is offered as-is without any warranty either expressed or
%% implied; without even the implied warranty of MERCHANTABILITY or
%% FITNESS FOR A PARTICULAR PURPOSE! 
%% User assumes all risk.
%% In no event shall the IEEE or any contributor to this code be liable for
%% any damages or losses, including, but not limited to, incidental,
%% consequential, or any other damages, resulting from the use or misuse
%% of any information contained here.
%%
%% All comments are the opinions of their respective authors and are not
%% necessarily endorsed by the IEEE.
%%
%% This work is distributed under the LaTeX Project Public License (LPPL)
%% ( http://www.latex-project.org/ ) version 1.3, and may be freely used,
%% distributed and modified. A copy of the LPPL, version 1.3, is included
%% in the base LaTeX documentation of all distributions of LaTeX released
%% 2003/12/01 or later.
%% Retain all contribution notices and credits.
%% ** Modified files should be clearly indicated as such, including  **
%% ** renaming them and changing author support contact information. **
%%*************************************************************************

\documentclass[journal]{IEEEtran}

% *** CITATION PACKAGES ***
\usepackage[style=ieee]{biblatex} 
\bibliography{example_bib.bib}    %your file created using JabRef

% *** MATH PACKAGES ***
\usepackage{amsmath}

% *** PDF, URL AND HYPERLINK PACKAGES ***
\usepackage{url}
% correct bad hyphenation here
\hyphenation{op-tical net-works semi-conduc-tor}
\usepackage{graphicx}  %needed to include png, eps figures
\usepackage{float}  % used to fix location of images i.e.\begin{figure}[H]

\begin{document}

% paper title
\title{Brief guidelines for a Lab report EEET2493\\ \small{Title of the session (you can be creative highlighting your findings)}}

% author names 
\author{Student name 1, s123456,
        Student name 2, s123456,
        Student name 3, s123456,
        {\small Names are to be centered in Times 
(or Times Roman) 12-point nonboldface. Leave two blank lines before your Abstract}}% &lt;-this % stops a space
        
% The report headers
\markboth{EEET2493 Biomedical Instrumentation. Lab. Report 1, March 2019}%do not delete next lines
{Shell \MakeLowercase{\textit{et al.}}: Bare Demo of IEEEtran.cls for IEEE Journals}

% make the title area
\maketitle

% As a general rule, do not put math, special symbols or citations
% in the abstract or keywords.
\begin{abstract}
Provide a summary of the session. What was done, 
what measurements were taken, brief methods, what calculations, brief conclusion.  The Abstract should be approximately 250 words or fewer, italicized, in 10-point Times (or Times Roman.) Please leave two spaces between the Abstract and the heading of your first section.
It should briefly summarize the essence of the paper and address the following areas without using specific subsection titles. Objective: Briefly state the problem or issue addressed, in language accessible to a general scientific audience. Technology or Method: Briefly summarize the technological innovation or method used to address the problem. Results: Provide a brief summary of the results and findings. Conclusions: Give brief concluding remarks on your outcomes. Detailed discussion of these aspects should be provided in the main body of the paper.
\end{abstract}

\begin{IEEEkeywords}
keywords, temperature, xxxx equation, etc.
\end{IEEEkeywords}

\section{Introduction}
% Here we have the typical use of a "W" for an initial drop letter
% and "RITE" in caps to complete the first word.
% You must have at least 2 lines in the paragraph with the drop letter
% (should never be an issue)

\IEEEPARstart{W}{rite} why is important to do this experiment, what background is needed, what technology has been used in this session, you can also talk briefly about what other technology exist but was not used here.
Then explain briefly how the experiment was conducted, what measurements were taken, what technology is used (acquisition system, sensors, software), if calculations were done, what calculations were done, what decisions were made, and what the final result was (explained in a concise way with words).
Writing “good” reports requires much thought, organization and editing but the rewards are
great. Those students who can master good technical writing skills will find greater success and
opportunity as professionals in industry. \\
You should become proficient at using your word processor for such items as page
numbers, formatting tables, creating graphs, inserting Greek symbols, and using
superscripts and subscripts.  \\

Remember that once you graduate, industry will require you to write well. In some cases, you will be involved in writing proposals or possibly final design reports. It is possible that you will be required to assist in writing user or maintenance manuals. Certainly, you will always be required to write short reports and memos detailing your activities. It is our objective to train you to write well by the time you have graduated. An excellent style manual for science writers is [3]. \\
You should become proficient at using the equation editor (Word has a built-in equation editor that can be accessed by “inserting object”. or you can use Latex editor). \\

Normal text is to be single-spaced in 10-point Times or Times Roman (or similar font), with 12-point interline spacing, in the two-column 
format. The first line of each paragraph is to be indented approximately 1/4 inch (approx. 0.7 cm), and the entire text is to be justified -- that is, flush left and flush right. Please do not place additional line spacing between paragraphs. Figure and table captions should be Helvetica 10-point boldface; callouts should be Helvetica 9-point 
nonboldface.  \\

TITLE AND HEADINGS. The main title should be in Times (or Times Roman) 14-point boldface centered over both columns. In the main title, please initially capitalize nouns, pronouns, verbs, adjectives, and adverbs; do not capitalize articles, coordinate conjunctions, and prepositions (unless the title begins with such a word). Initially capitalize only the first word in first-, second-, and third-order headings. Leave two blank lines before author names(s)/affiliation(s).

\section{Materials and Methods}
List of materials used and how these were used / connected (good opportunity to present block diagrams to show connections).
Use good drafting practice when producing figures, graphs, drawings, or schematics and label them for easy
reference. Include schematics for any circuits. 
If using latex use "~cite" command to cite references~\cite{Kulkarni2015}.

What calculations were done. List and number your equations (Eq.~\ref{equation:force}) to be able to referred them in the text. Equations are centered and the equation numbers are right justified. The equation number is placed in ( ). Be sure that the symbols in your equation have been defined. See example Equation~\ref{equation:force}. 
\begin{equation}
    F = ma
    \label{equation:force}
\end{equation}
Where F equals to force, m to mass and a to acceleration.  

\section{Results}
Show plots of any data collected and describe with words what your plots are showing. Describe the relationship between variables and time. Remember to number all your figures.  This is the most critical part affect the technical achievement.\\
No picture, table, schematic, or graph should appear without a name (generally of the form Fig.1 o Table 1). %~\ref{fig:ecg} or Table~\ref{table:Exps}), 
None should appear without a reference to them by name in the main body of the writing.
All figures and tables must be discussed in the text, including what it is, significant observations, and analysis. \\
Capitalize “Table” and “Fig.” any time they are accompanied by specific table or figure numbers. Examples: “The measured data are plotted in Fig. 2. The figure shows a linear relationship in....”. “The table shows …” vs. “The data of Table 3…” \\

\begin{table}[!ht] %[H]
\centering
\label{table:Exps}
\begin{tabular}{ll}
Student &amp;  Max Temperature \\ \hline
aabbbccc &amp;  $35^{\circ}$   \\
eeeddd &amp;   $54^{\circ}$ \\
eeeddd &amp;   $54^{\circ}$ \\
\end{tabular}
\caption{Temperature measurements performed for session 1.}
\end{table}

%you can use a table generator from here: https://www.tablesgenerator.com/#

Use your word processor to make “real tables” (i.e., boxed in, etc.). Center all tables and include a heading and caption with the appropriate table number below each table. For example, “Table 1: Temperature measurements performed for session 1.” 

Figures must be centered, and the figure number and caption is centered beneath the
figure. For example, “Figure~\ref{fig:ecg}”. 

\begin{figure}[H]%[!ht]
\begin {center}
\includegraphics[width=0.45\textwidth]{images/ecg.png}
\caption{Illustrations, graphs, and photographs may fit across both columns, if necessary. Your artwork must be in place in the article.}
\label{fig:ecg}
\end {center}
\end{figure}

Always spell out table or Table. Give abbreviation of Figure, i.e., Fig., when used in
the middle to end of sentence, but spell it out when used at the very start of the
sentence. \\


All graphs must be done with a computer (i.e., spreadsheet software such as
Microsoft Excel or even Matlab.). Do not include hand drawn graphs unless
specifically instructed to do so.  \\
Include a leading zero when a number’s magnitude is less than 1 (use 0.83 instead of
writing .83). \\
Use your word processor for Greek symbols for common engineering quantities as $\beta$, $\pi$, $\gamma$ ,$\Omega$ . 

\section{Discussion and Summary}
Discuss any interesting result related to the materials used or to any claim from the introduction. Discuss your measurements using engineering terms (accuracy, precision, resolution, etc).  Give technical conclusions. Restate the main objectives and how or to what degree they were achieved. What principles, laws and/or theory
were validated by the experiment? Describe some applications of your results and comment any possible recommended future work.



% if have a single appendix:
%\appendix[Proof of the Zonklar Equations]
% or
%\appendix  % for no appendix heading
% do not use \section anymore after \appendix, only \section*
% is possibly needed

% use appendices with more than one appendix
% then use \section to start each appendix
% you must declare a \section before using any
% \subsection or using \label (\appendices by itself
% starts a section numbered zero.)
%


\appendices
\section{Hand calculations (or name your title for appendix subtitle)}
List any extra evidence such as photos of the session, that may help you support your claims.
You can include all hand calculations, extra graphs and plots, simulation results, etc. 

% use section* for acknowledgment
\section*{Acknowledgment}
The authors would like to thank...



% references section

% can use a bibliography generated by BibTeX as a .bbl file
% BibTeX documentation can be easily obtained at:
% http://mirror.ctan.org/biblio/bibtex/contrib/doc/
% The IEEEtran BibTeX style support page is at:
% http://www.michaelshell.org/tex/ieeetran/bibtex/
%\bibliographystyle{IEEEtran}
% argument is your BibTeX string definitions and bibliography database(s)
%\bibliography{IEEEabrv,../bib/paper}
%
% &lt;OR&gt; manually copy in the resultant .bbl file
% set second argument of \begin to the number of references
% (used to reserve space for the reference number labels box)

%use following command to generate the list of cited references

\printbibliography


Examples of references:  \\[0.001in]

Example of data book:\\[0.1in]
[2] National Operational Amplifiers Databook. Santa Clara: National Semiconductor
Corporation, 1995 Edition, p. I-54. \\[0.1in]
Example of textbook: \\[0.1in]
[3]M. Young, The Technical Writer’s Handbook. Mill Valley, CA: University Science, 1989.\\[0.1in]
Example of scientific journal paper:\\[0.1in]
[4] J.W. Smith, L.S. Alans and D.K. Jones, “An operational amplifier approach to
active cable modeling”, IEEE Transactions on Modeling, vol. 4, no. 2, 1996, pp.
128-132.\\[0.1in]
Example of conference paper proceedings:\\[0.1in]
[5] J.W. Smith, L.S. Alans and D.K. Jones, “Active cable models for lossy
transmission line circuits”, in Proc. 1995 IEEE Modeling Symposium, 1996, pp.
1086-89.\\[0.1in]

Example of Internet web page:\\[0.1in]
[6] Approximate material properties in isotropic materials. Milpitas, CA: Specialty
Engineering Associates, Inc. web site: www.ultrasonic.com, downloaded Aug. 20,
2001. 

List and number all bibliographical 
references at the end of your paper in {\bf 9 or 10 point} Times, with 10-point interline spacing. When referenced within the text, enclose the citation number in square brackets, for example [1]. \\
Use IEEE format. Cite any external work that you used (data sheets, text books, Wikipedia articles, . . . ). If you get a formula from a Wikipedia article, you must cite the article, giving the title, the URL, and the data you accessed the article as a minimum. If you copy a figure, not only must you cite the article you copied from, but you must give explicit figure credit in the caption for the figure: This image copied from . . . . If you modify a figure or base your figure on one that has been published elsewhere, you still need to give credit in the caption: This image adapted from . . . .\\[0.1in]

% that's all folks
\end{document}

