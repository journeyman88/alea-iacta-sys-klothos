/*
 * Copyright 2020 Marco Bignami.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.unknowndomain.alea.systems.klothos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.unknowndomain.alea.messages.MsgBuilder;
import net.unknowndomain.alea.messages.MsgStyle;
import net.unknowndomain.alea.roll.GenericResult;

/**
 *
 * @author journeyman
 */
public class KlothosResults extends GenericResult
{
    private final List<Integer> actionResults;
    private final Integer fateDice;
    private int total = 0;
    private int luck = 0;
    private int misfortune = 0;
    
    public KlothosResults(List<Integer> actionResults, Integer fateDice)
    {
        List<Integer> tmp = new ArrayList<>(actionResults.size());
        tmp.addAll(actionResults);
        this.actionResults = Collections.unmodifiableList(tmp);
        this.fateDice = fateDice;
    }
    
    public void addLuck()
    {
        luck++;
    }
    
    public void addMisfortune()
    {
        misfortune++;
    }
    
    public List<Integer> getActionResults()
    {
        return actionResults;
    }
    
    @Override
    protected void formatResults(MsgBuilder messageBuilder, boolean verbose, int indentValue)
    {
        String indent = getIndent(indentValue);
        messageBuilder.append("Result: ").append(total);
        if (misfortune > 0)
        {
            messageBuilder.append(" | Misfortune : ").append(misfortune, MsgStyle.BOLD);
        }
        if (luck > 0)
        {
            messageBuilder.append(" | Luck : ").append(luck, MsgStyle.BOLD);
        }
        messageBuilder.appendNewLine();
        if (verbose)
        {
            messageBuilder.append(indent).append("Roll ID: ").append(getUuid()).appendNewLine();
            messageBuilder.append("Dice Results: ").append(" [ ");
            for (Integer t : getActionResults())
            {
                messageBuilder.append(t).append(" ");
            }
            messageBuilder.append("]").appendNewLine();
            if (fateDice != null)
            {
                messageBuilder.append("Fate Dice: ").append(" [ ");
                messageBuilder.append(fateDice).append(" ");
                messageBuilder.append("]").appendNewLine();
            }
        }
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

}
