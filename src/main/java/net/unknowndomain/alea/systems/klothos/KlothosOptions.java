/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unknowndomain.alea.systems.klothos;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.unknowndomain.alea.systems.RpgSystemOptions;
import net.unknowndomain.alea.systems.annotations.RpgSystemData;
import net.unknowndomain.alea.systems.annotations.RpgSystemOption;


/**
 *
 * @author journeyman
 */
@RpgSystemData(bundleName = "net.unknowndomain.alea.systems.klothos.RpgSystemBundle")
public class KlothosOptions extends RpgSystemOptions
{
    @RpgSystemOption(name = "approach", shortcode = "a", description = "klothos.options.approach", argName = "approachModifier")
    private Integer approach;
    @RpgSystemOption(name = "specialization", shortcode = "s", description = "klothos.options.specialization", argName = "specValue")
    private Integer specialization;
    @RpgSystemOption(name = "enemy-spec", shortcode = "e", description = "klothos.options.enemy-spec")
    private boolean enemySpec;
    @RpgSystemOption(name = "bond", shortcode = "b", description = "klothos.options.bond")
    private boolean bond;
    
                        
    @Override
    public boolean isValid()
    {
        return !(isHelp());
    }

    public Integer getApproach()
    {
        return approach;
    }

    public Integer getSpecialization()
    {
        return specialization;
    }
    
    public boolean isEnemySpec()
    {
        return enemySpec;
    }
    
    public Integer getTotal()
    {
        int tot = 0;
        if (approach != null)
        {
            tot += approach;
        }
        if (specialization != null)
        {
            tot += specialization;
        }
        if (enemySpec)
        {
            tot += 2;
        }
        return tot;
    }

    public Collection<KlothosModifiers> getModifiers()
    {
        Set<KlothosModifiers> mods = new HashSet<>();
        if (isVerbose())
        {
            mods.add(KlothosModifiers.VERBOSE);
        }
        if (isEnemySpec())
        {
            mods.add(KlothosModifiers.ENEMY_SPEC);
        }
        if (isBond())
        {
            mods.add(KlothosModifiers.BONDED_ALLY);
        }
        return mods;
    }

    public boolean isBond()
    {
        return bond;
    }
    
}