package srttbacon.plugin.Class;

import cn.nukkit.Player;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.math.Vector3;

public class Particle
{
	public static void Particle_Circle(Player p, Vector3 Pos, ParticleEffect effect)
	{
        for(int i = 0; i < 360; i+=10)
        {
            Vector3 Particle_Pos = Pos.clone();
            Particle_Pos.z = Particle_Pos.z + Math.sin(i)* 3.5 + 0.2;
            Particle_Pos.x = Particle_Pos.x + Math.cos(i)* 3.5 + 0.2;
			p.level.addParticleEffect(Particle_Pos, effect);
        }
	}
	public static void Particle_Circle(Player p, Vector3 Pos, ParticleEffect effect, double Value)
	{
        for(int i = 0; i < 360; i+=10)
        {
            Vector3 Particle_Pos = Pos.clone();
            Particle_Pos.z = Particle_Pos.z + Math.sin(i)* Value + 0.2;
            Particle_Pos.x = Particle_Pos.x + Math.cos(i)* Value + 0.2;
			p.level.addParticleEffect(Particle_Pos, effect);
        }
	}
}