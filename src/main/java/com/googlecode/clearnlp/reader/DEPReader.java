/**
* Copyright (c) 2009-2012, Regents of the University of Colorado
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/
package com.googlecode.clearnlp.reader;

import java.util.List;

import com.googlecode.clearnlp.dependency.DEPFeat;
import com.googlecode.clearnlp.dependency.DEPNode;
import com.googlecode.clearnlp.dependency.DEPTree;


/**
 * Dependency tree reader.
 * @since 1.0.0
 * @author Jinho D. Choi ({@code choijd@colorado.edu})
 */
public class DEPReader extends AbstractColumnReader<DEPTree>
{
	private int i_id;
	private int i_form;
	private int i_lemma;
	private int i_pos;
	private int i_feats;
	private int i_headId;
	private int i_deprel;
	
	/**
	 * Constructs a dependency reader.
	 * @param iId the column index of the node ID field.
	 * @param iForm the column index of the word-form field.
	 * @param iLemma the column index of the lemma field.
	 * @param iPos the column index of the POS field.
	 * @param iFeats the column index of the feats field.
	 * @param iHeadId the column index of the head ID field.
	 * @param iDeprel the column index of the dependency label field.
	 */
	public DEPReader(int iId, int iForm, int iLemma, int iPos, int iFeats, int iHeadId, int iDeprel)
	{
		init(iId, iForm, iLemma, iPos, iFeats, iHeadId, iDeprel);
	}
	
	/**
	 * Initializes column indexes of fields.
	 * @param iId the column index of the node ID field.
	 * @param iForm the column index of the word-form field.
	 * @param iLemma the column index of the lemma field.
	 * @param iPos the column index of the POS field.
	 * @param iFeats the column index of the feats field.
	 * @param iHeadId the column index of the head ID field.
	 * @param iDeprel the column index of the dependency label field.
	 */
	public void init(int iId, int iForm, int iLemma, int iPos, int iFeats, int iHeadId, int iDeprel)
	{
		i_id     = iId;
		i_form   = iForm;
		i_lemma  = iLemma;
		i_pos    = iPos;
		i_feats  = iFeats;
		i_headId = iHeadId;
		i_deprel = iDeprel;
	}
	
	/* (non-Javadoc)
	 * @see edu.colorado.clear.reader.AbstractReader#next()
	 */
	public DEPTree next()
	{
		DEPTree tree = null;
		
		try
		{
			List<String[]> lines = readLines();
			if (lines == null)	return null;
			
			tree = getDEPTree(lines);
		}
		catch (Exception e) {e.printStackTrace();}
		
		return tree;
	}
	
	/**
	 * Returns a dependency tree from the input lines.
	 * @param lines the input lines.
	 * @return a dependency tree from the input lines.
	 */
	protected DEPTree getDEPTree(List<String[]> lines)
	{
		int id, headId, i, size = lines.size();
		String form, lemma, pos, deprel;
		DEPFeat feats;
		String[] tmp;
		DEPNode node;
		
		DEPTree tree = new DEPTree();
		
		// initialize place holders
		for (i=0; i<size; i++)
			tree.add(new DEPNode());
		
		for (i=0; i<size; i++)
		{
			tmp   = lines.get(i);
			id    = Integer.parseInt(tmp[i_id]);
			form  = tmp[i_form];
			lemma = tmp[i_lemma];
			pos   = tmp[i_pos];
			feats = new DEPFeat(tmp[i_feats]);
			
			node = tree.get(id);
			node.init(id, form, lemma, pos, feats);
					
			if (i_headId >= 0 && !tmp[i_headId].equals(AbstractColumnReader.BLANK_COLUMN))
			{
				headId = Integer.parseInt(tmp[i_headId]);
				deprel = tmp[i_deprel];
				
				node.setHead(tree.get(headId), deprel);
			}
		}
		
		return tree;
	}
}
