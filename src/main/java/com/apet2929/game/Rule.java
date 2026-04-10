package com.apet2929.game;

import com.apet2929.game.particles.ParticleType;
import org.joml.Vector2i;

public class Rule {
    int[][] beforeNeighbors;
    int[][] afterNeighbors; // what the result will be
    ParticleType primaryTarget;

//    HashMap<ParticleType, ArrayList<Particle>> foo;
    /* scan_grid_for_applicable_instances() {
        for Particle sand : foo[SAND] {

        }
        for x, y {

            if grid[x,y].type == primaryTarget {
                if(gridMatches(beforeNeighbors)) doTheThing();
            }
        }
    }
    * */


//    public void shouldApply(World world, Vector2i pos){
//        if(matches(pos, beforeNeighbors, world)) {
//
//        }
//    }

    /*
    Foo     -> Water
    Water       Foo

    Water   -> Nothing
    Fire       Water

    Foo
    Water
    Fire

    Nothing
    Sand
    Nothing


    


    for(Rule rule in rules) {
        scan_grid_for_applicable_instances()
        for(instance in instances){
            scan_grid
        }
        roll_to_apply_rule(rule)
    }
    Grid nextTickGrid = copy(grid);
    for(pos in grid){
        for(rule in rules){
            if(role_to_apply(rule, grid) and rule.applies(pos)) apply(rule, nextTickGrid); break;
        }
    }
     */

}
