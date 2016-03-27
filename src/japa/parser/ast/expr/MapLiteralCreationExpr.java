/*
 * Copyright (C) 2007 Júlio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 05/10/2006
 */
package japa.parser.ast.expr;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.GenericVisitor;
import japa.parser.ast.visitor.VoidVisitor;

import java.util.List;
import java.util.Map;

/**
 * @author Julio Vilmar Gesser
 */
public final class MapLiteralCreationExpr extends Expression {

    private final Expression scope;

    private final ClassOrInterfaceType type;

    private final List<Type> typeArgs;

    private final List<Expression> args;

    private final Map<Object, Object> mapEntries;

    public MapLiteralCreationExpr(int line, int column, Expression scope, ClassOrInterfaceType type, List<Type> typeArgs, List<Expression> args, Map<Object, Object> mapEntries) {
        super(line, column);
        this.scope = scope;
        this.type = type;
        this.typeArgs = typeArgs;
        this.args = args;
        this.mapEntries = mapEntries;
    }

    public Expression getScope() {
        return scope;
    }

    public ClassOrInterfaceType getType() {
        return type;
    }

    public List<Type> getTypeArgs() {
        return typeArgs;
    }

    public List<Expression> getArgs() {
        return args;
    }

    public Map<Object, Object> getMapEntries() {
        return mapEntries;
    }

    @Override
    public <A> void accept(VoidVisitor<A> v, A arg) {
        v.visit(this, arg);
    }

    @Override
    public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
        return v.visit(this, arg);
    }

}
