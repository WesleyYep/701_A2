/*
 * Copyright (C) 2007 J�lio Vilmar Gesser.
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
package japa.parser.ast.visitor;

import japa.parser.ast.BlockComment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EmptyMemberDeclaration;
import japa.parser.ast.body.EmptyTypeDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MapLiteralCreationExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.SuperMemberAccessExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.EmptyStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;
import se701.A2SemanticsException;
import symtab.ClassSymbol;
import symtab.GlobalScope;
import symtab.MethodSymbol;
import symtab.Scope;
import symtab.Symbol;
import symtab.VariableSymbol;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Wesley Yep
 */

public final class ResolvingVisitor implements VoidVisitor<Object> {

    private void printMembers(List<BodyDeclaration> members, Object arg) {
        for (BodyDeclaration member : members) {
            member.accept(this, arg);
        }
    }

    private void printMemberAnnotations(List<AnnotationExpr> annotations, Object arg) {
        if (annotations != null) {
            for (AnnotationExpr a : annotations) {
                a.accept(this, arg);
            }
        }
    }

    private void printAnnotations(List<AnnotationExpr> annotations, Object arg) {
        if (annotations != null) {
            for (AnnotationExpr a : annotations) {
                a.accept(this, arg);
            }
        }
    }

    private void printTypeArgs(List<Type> args, Object arg) {
        if (args != null) {
            for (Iterator<Type> i = args.iterator(); i.hasNext();) {
                Type t = i.next();
                t.accept(this, arg);
            }
        }
    }

    private void printTypeParameters(List<TypeParameter> args, Object arg) {
        if (args != null) {
            for (Iterator<TypeParameter> i = args.iterator(); i.hasNext();) {
                TypeParameter t = i.next();
                t.accept(this, arg);
            }
        }
    }
    
    public void visit(Node n, Object arg) {
        throw new IllegalStateException(n.getClass().getName());
    }

    public void visit(CompilationUnit n, Object arg) {
        if (n.getPakage() != null) {
            n.getPakage().accept(this, arg);
        }
        if (n.getImports() != null) {
            for (ImportDeclaration i : n.getImports()) {
                i.accept(this, arg);
            }
        }
        if (n.getTypes() != null) {
            for (Iterator<TypeDeclaration> i = n.getTypes().iterator(); i.hasNext();) {
                i.next().accept(this, arg);
            }
        }
    }

    public void visit(PackageDeclaration n, Object arg) {
        printAnnotations(n.getAnnotations(), arg);
    	n.getName().accept(this, arg);
    }

    public void visit(NameExpr n, Object arg) {
    	if (n.getCurrentScope() == null) {
    		return; //if there's no scope, this is probably something like a package declaration
    	}
    	Symbol symOfVariable = n.getCurrentScope().resolve(n.getName());
        if(symOfVariable == null){
        	throw new A2SemanticsException(n.getName() + " on line " + n.getBeginLine() + " has not been defined");
        }
    }

    public void visit(QualifiedNameExpr n, Object arg) {
        n.getQualifier().accept(this, arg);
    }

    public void visit(ImportDeclaration n, Object arg) {
        n.getName().accept(this, arg);
    }

    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        printTypeParameters(n.getTypeParameters(), arg);

        if (n.getExtends() != null) {
            for (Iterator<ClassOrInterfaceType> i = n.getExtends().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
            }
        }

        if (n.getImplements() != null) {
            for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
            }
        }
        if (n.getMembers() != null) {
            printMembers(n.getMembers(), arg);
        }
    }

    public void visit(EmptyTypeDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
    }

    public void visit(JavadocComment n, Object arg) {

    }

    public void visit(ClassOrInterfaceType n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        printTypeArgs(n.getTypeArgs(), arg);
    }

    public void visit(TypeParameter n, Object arg) {
        if (n.getTypeBound() != null) {
            for (Iterator<ClassOrInterfaceType> i = n.getTypeBound().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
            }
        }
    }

    public void visit(PrimitiveType n, Object arg) {

    }

    public void visit(ReferenceType n, Object arg) {
        n.getType().accept(this, arg);
    }

    public void visit(WildcardType n, Object arg) {
        if (n.getExtends() != null) {
            n.getExtends().accept(this, arg);
        }
        if (n.getSuper() != null) {
            n.getSuper().accept(this, arg);
        }
    }

    public void visit(FieldDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        n.getType().accept(this, arg);
        
        String[] types = getBaseTypeAndGenerics(n.getType().toString());
        Symbol symOfVariable = n.getCurrentScope().resolve(types[0]);
        if(symOfVariable == null){
        	throw new A2SemanticsException(types[0] + " on line " + n.getType().getBeginLine() + " is not a defined type");
        }
        if(!(symOfVariable instanceof symtab.Type)){
        	throw new A2SemanticsException(types[0] + " on line " + n.getType().getBeginLine() + " is not a valid type");
        }
        for (Iterator<VariableDeclarator> i = n.getVariables().iterator(); i.hasNext();) {
            VariableDeclarator v = i.next();
            v.accept(this, arg);
            
            symtab.Type typeOfLeft = (symtab.Type)symOfVariable;
            symtab.Type typeOfRight = getTypeOfExpression(n, v.getInit());
            if(typeOfRight != null){
	            if(!isValidFor(typeOfRight, typeOfLeft)){
	        		throw new A2SemanticsException("Cannot convert from " + typeOfRight.getName() + " to " + typeOfLeft.getName() + " on line " + n.getType().getBeginLine());
	            }
            }            
        }
    }
    
    //added this helper method to get the types associated with a declaration of a map
    public String[] getBaseTypeAndGenerics(String type) {
    	String[] array;
    	String regex = "(.*)<(.*)(,(.*))*>";
    	Pattern pattern = Pattern.compile(regex);
    	Matcher matcher = pattern.matcher(type.trim());
    	if (matcher.matches()) {
    		array = new String[] {matcher.group(1), matcher.group(2)};
    	} else {
    		array = new String[] {type};
    	}
    	return array;
    }

    public void visit(VariableDeclarator n, Object arg) {
        n.getId().accept(this, arg);
        if (n.getInit() != null) {
            n.getInit().accept(this, arg);
        }
    }

    public void visit(VariableDeclaratorId n, Object arg) {

    }

    public void visit(ArrayInitializerExpr n, Object arg) {
        if (n.getValues() != null) {
            for (Iterator<Expression> i = n.getValues().iterator(); i.hasNext();) {
                Expression expr = i.next();
                expr.accept(this, arg);
            }
        }
    }

    public void visit(VoidType n, Object arg) {

    }

    public void visit(ArrayAccessExpr n, Object arg) {
        n.getName().accept(this, arg);
        n.getIndex().accept(this, arg);
    }

    public void visit(ArrayCreationExpr n, Object arg) {
        n.getType().accept(this, arg);
        printTypeArgs(n.getTypeArgs(), arg);

        if (n.getDimensions() != null) {
            for (Expression dim : n.getDimensions()) {
                dim.accept(this, arg);
            }
        } else {
            n.getInitializer().accept(this, arg);
        }
    }

    public void visit(AssignExpr n, Object arg) {
        n.getTarget().accept(this, arg);
        n.getValue().accept(this, arg);

        Symbol symOfVariable = n.getTarget().getCurrentScope().resolve(((NameExpr) n.getTarget()).getName());
        if(symOfVariable == null){
        	throw new A2SemanticsException(((NameExpr) n.getTarget()).getName() + " on line " + n.getBeginLine() + " has not been defined");
        }
    	symtab.Type typeOfLeft = symOfVariable.getType();
    	symtab.Type typeOfRight = getTypeOfExpression(n, n.getValue());
    	if(typeOfRight != null) {
    		if(!isValidFor(typeOfRight, typeOfLeft)){
    			throw new A2SemanticsException("Cannot convert from " + typeOfRight.getName() + " to " + typeOfLeft.getName() + " on line " + n.getTarget().getBeginLine());
    		}
    	}
    }

    public void visit(BinaryExpr n, Object arg) {
        n.getLeft().accept(this, arg);
        n.getRight().accept(this, arg);
    }

    public void visit(CastExpr n, Object arg) {
        n.getType().accept(this, arg);
        n.getExpr().accept(this, arg);
    }

    public void visit(ClassExpr n, Object arg) {
        n.getType().accept(this, arg);
    }

    public void visit(ConditionalExpr n, Object arg) {
        n.getCondition().accept(this, arg);
        n.getThenExpr().accept(this, arg);
        n.getElseExpr().accept(this, arg);
    }

    public void visit(EnclosedExpr n, Object arg) {
        n.getInner().accept(this, arg);
    }

    public void visit(FieldAccessExpr n, Object arg) {
        n.getScope().accept(this, arg);
    }

    public void visit(InstanceOfExpr n, Object arg) {
        n.getExpr().accept(this, arg);
        n.getType().accept(this, arg);
    }

    public void visit(CharLiteralExpr n, Object arg) {
    }

    public void visit(DoubleLiteralExpr n, Object arg) {
    }

    public void visit(IntegerLiteralExpr n, Object arg) {
    }

    public void visit(LongLiteralExpr n, Object arg) {
    }

    public void visit(IntegerLiteralMinValueExpr n, Object arg) {
    }

    public void visit(LongLiteralMinValueExpr n, Object arg) {
    }

    public void visit(StringLiteralExpr n, Object arg) {
    }

    public void visit(BooleanLiteralExpr n, Object arg) {
    }

    public void visit(NullLiteralExpr n, Object arg) {
    }

    public void visit(ThisExpr n, Object arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
        }
    }

    public void visit(SuperExpr n, Object arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
        }
    }

    public void visit(MethodCallExpr n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        printTypeArgs(n.getTypeArgs(), arg);
        Scope className = null;
        if (n.getScope() != null) {
        	String t = n.getScope().toString();
			Symbol sym = n.getCurrentScope().resolve(t);
			if(sym == null){
				throw new A2SemanticsException(t + " is not defined on line " + n.getBeginLine());
			}
			if (sym.getType() instanceof Scope) {
				className = (Scope)sym.getType();
				if(className == null){
					throw new A2SemanticsException("Unknown method on line " + n.getBeginLine());
				}
			}
        } else {
        	className = n.getCurrentScope();
        }
        if (className != null) {
			Symbol sym = className.resolve(n.getName());
			if(sym == null){
				throw new A2SemanticsException(n.getName() + "() is not defined on line " + n.getBeginLine());
			} else if (sym instanceof VariableSymbol) {
				throw new A2SemanticsException(n.getName()+ " is a variable but used as a method on line " + n.getBeginLine());
			}
        }
		
        if (n.getArgs() != null) {
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
            }
        }
    }

    public void visit(ObjectCreationExpr n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        printTypeArgs(n.getTypeArgs(), arg);

        n.getType().accept(this, arg);

        if (n.getArgs() != null) {
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
            }
        }
        if (n.getAnonymousClassBody() != null) {
            printMembers(n.getAnonymousClassBody(), arg);
        }
    }
    
    public void visit(MapLiteralCreationExpr n, Object arg) {
    	 if (n.getScope() != null) {
             n.getScope().accept(this, arg);
         }
         n.getType().accept(this, arg);
    }


    public void visit(SuperMemberAccessExpr n, Object arg) {
    }

    public void visit(UnaryExpr n, Object arg) {
        n.getExpr().accept(this, arg);
    }

    public void visit(ConstructorDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        printTypeParameters(n.getTypeParameters(), arg);
        if (n.getParameters() != null) {
            for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
                Parameter p = i.next();
                p.accept(this, arg);
            }
        }

        if (n.getThrows() != null) {
            for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
                NameExpr name = i.next();
                name.accept(this, arg);
            }
        }
        n.getBlock().accept(this, arg);
    }

    public void visit(MethodDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        printTypeParameters(n.getTypeParameters(), arg);
        n.getType().accept(this, arg);
        if (n.getParameters() != null) {
            for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
                Parameter p = i.next();
                p.accept(this, arg);
            }
        }

        if (n.getThrows() != null) {
            for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
                NameExpr name = i.next();
                name.accept(this, arg);
            }
        }
        if (n.getBody() == null) {
        } else {
            n.getBody().accept(this, arg);
        }
    }

    public void visit(Parameter n, Object arg) {
    	Symbol symOfVariable = n.getCurrentScope().resolve(n.getType().toString());
        if(symOfVariable == null){
        	throw new A2SemanticsException(n.getType().toString() + " on line " + n.getType().getBeginLine() + " is not a defined type");
        }
        if(!(symOfVariable instanceof symtab.Type)){
        	throw new A2SemanticsException(n.getType().toString() + " on line " + n.getType().getBeginLine() + " is not a valid type");
        }
        printAnnotations(n.getAnnotations(), arg);
        n.getType().accept(this, arg);
        n.getId().accept(this, arg);
    }

    public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
        if (n.isThis()) {
            printTypeArgs(n.getTypeArgs(), arg);
        } else {
            if (n.getExpr() != null) {
                n.getExpr().accept(this, arg);
            }
            printTypeArgs(n.getTypeArgs(), arg);
        }
        if (n.getArgs() != null) {
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
            }
        }
    }
   

    public void visit(VariableDeclarationExpr n, Object arg) {
        printAnnotations(n.getAnnotations(), arg);
        n.getType().accept(this, arg);
        
        String[] types = getBaseTypeAndGenerics(n.getType().toString());
        Symbol symOfVariable = n.getCurrentScope().resolve(types[0]);
        if(symOfVariable == null){
        	throw new A2SemanticsException(types[0] + " on line " + n.getType().getBeginLine() + " is not a defined type");
        }
        if(!(symOfVariable instanceof symtab.Type)){
        	throw new A2SemanticsException(types[0] + " on line " + n.getType().getBeginLine() + " is not a valid type");
        }
        
        for (Iterator<VariableDeclarator> i = n.getVars().iterator(); i.hasNext();) {
            VariableDeclarator v = i.next();
            v.accept(this, arg);
           
            symtab.Type typeOfLeft = (symtab.Type)symOfVariable;
            symtab.Type typeOfRight = getTypeOfExpression(n, v.getInit());
            if(typeOfRight != null){
	            if(!isValidFor(typeOfRight, typeOfLeft)){
	        		throw new A2SemanticsException("Cannot convert from " + typeOfRight.getName() + " to " + typeOfLeft.getName() + " on line " + n.getType().getBeginLine());
	            }
            }   
        }
    }
    
    /**
     * This method is used since some types are valid to be used in place of others
     * for example, int can be used in place of long, double, and char types (but not neccessarily vice versa)
     * 
     * @param typeOfRight
     * @param typeOfLeft
     * @return
     */
    public boolean isValidFor(symtab.Type typeOfRight, symtab.Type typeOfLeft) {
    	if (typeOfLeft.getName().equals(typeOfRight.getName()) 
			|| typeOfRight.getName().equals("null") && typeOfLeft.getName().equals("String")
			|| typeOfRight.getName().equals("null") && typeOfLeft instanceof ClassSymbol
			|| typeOfRight.getName().equals("int") && typeOfLeft.getName().equals("long") //ints can count as long
			|| typeOfRight.getName().equals("int") && typeOfLeft.getName().equals("double") //ints can count as double
			|| typeOfRight.getName().equals("int") && typeOfLeft.getName().equals("Integer") 
			|| typeOfRight.getName().equals("int") && typeOfLeft.getName().equals("char") //ints can count as char
			|| typeOfRight.getName().equals("char") && typeOfLeft.getName().equals("int") //chars can count as int
			|| typeOfRight.getName().equals("char") && typeOfLeft.getName().equals("Character") 
			|| typeOfRight.getName().equals("char") && typeOfLeft.getName().equals("double") //chars can count as double
			|| typeOfRight.getName().equals("long") && typeOfLeft.getName().equals("Long") 
			|| typeOfRight.getName().equals("double") && typeOfLeft.getName().equals("Double") 
			|| typeOfRight.getName().equals("boolean") && typeOfLeft.getName().equals("Boolean") 
			) { 
    			return true;
    	} 
    	return false;
    }

    private symtab.Type getTypeOfExpression(Node n, Expression init) {
    	symtab.Type type = null;
    	if(init != null){
    		Symbol sym = null;
    		if(init.getClass() == NameExpr.class){
    			sym = n.getCurrentScope().resolve(init.toString());
    			if(sym == null){
    				throw new A2SemanticsException(init + " is not defined on line " + init.getBeginLine());
    			}
    			if(!(sym.getType() instanceof symtab.Type)){
    				throw new A2SemanticsException(init + " is not valid on line " + init.getBeginLine());
    			}
    			if (sym.getLineNumber() > n.getBeginLine()) {
    				throw new A2SemanticsException(sym.getName() + " is not defined on line " + init.getBeginLine());
    			}
    			type = sym.getType();
    		}else{
    			//NOTE: IntegerLiteralExpr extends StringLiteralExpr, so must check IntegerLiteralExpr first
    			if (init.getClass() == LongLiteralExpr.class) {
    				sym = n.getCurrentScope().resolve("long");
    			} else if(init.getClass() == IntegerLiteralExpr.class){
    				sym = n.getCurrentScope().resolve("int");
    			}else if (init.getClass() == StringLiteralExpr.class){
    				sym = n.getCurrentScope().resolve("String");
    			} else if (init.getClass() == BooleanLiteralExpr.class) {
    				sym = n.getCurrentScope().resolve("boolean");
    			} else if (init.getClass() == CharLiteralExpr.class) {
    				sym = n.getCurrentScope().resolve("char");
    			} else if (init.getClass() == DoubleLiteralExpr.class) {
    				sym = n.getCurrentScope().resolve("double");
    			} else if (init.getClass() == NullLiteralExpr.class) {
    				sym = n.getCurrentScope().resolve("null");
    			} 
    			//for my map feature
    			else if (init.getClass() == MapLiteralCreationExpr.class) {
    				sym = n.getCurrentScope().resolve("Map");
    			} else if (init.getClass() == MapLiteralCreationExpr.class) {
    				sym = n.getCurrentScope().resolve("Entry");
    			}
    			//other primitive types (and others?)
    			else if (init.getClass() == ObjectCreationExpr.class) {
    				String t = ((ObjectCreationExpr) init).getType().getName();
    				sym = n.getCurrentScope().resolve(t);
        			if(sym == null){
        				throw new A2SemanticsException(t + " is not defined on line " + init.getBeginLine());
        			}
    			}
    			else if (init.getClass() == FieldAccessExpr.class) {
    				String t = ((FieldAccessExpr) init).getScope().toString();
    				sym = n.getCurrentScope().resolve(t);
        			if(sym == null){
        				throw new A2SemanticsException(t + " is not defined on line " + init.getBeginLine());
        			}
        			Scope sss = (Scope)sym.getType();
        			sym = sss.resolve(((FieldAccessExpr) init).getField());
        			if(sym == null){
        				throw new A2SemanticsException(((FieldAccessExpr) init).getField() + " is not defined on line " + init.getBeginLine());
        			} else if (sym instanceof MethodSymbol) {
        				throw new A2SemanticsException(((FieldAccessExpr) init).getField() + " is a method but used as a field on line " + init.getBeginLine());
        			}
        			sym = (Symbol) sym.getType();
    			} else if (init.getClass() == MethodCallExpr.class) {
    				String t = ((MethodCallExpr) init).getScope().toString();
    				sym = n.getCurrentScope().resolve(t);
        			if(sym == null){
        				throw new A2SemanticsException(t + " is not defined on line " + init.getBeginLine());
        			}
        			Scope className = (Scope)sym.getType();
        			if(className == null){
        				throw new A2SemanticsException("Unknown method on line " + init.getBeginLine());
        			}
        			sym = className.resolve(((MethodCallExpr) init).getName());
        			if(sym == null){
        				throw new A2SemanticsException(((MethodCallExpr) init).getName() + "() is not defined on line " + init.getBeginLine());
        			} else if (sym instanceof VariableSymbol) {
        				throw new A2SemanticsException(((MethodCallExpr) init).getName()+ " is a variable but used as a method on line " + init.getBeginLine());
        			}
        			sym = (Symbol) sym.getType();
    			}
    			else{
    				System.out.println("Add " + init.getClass() + " to getTypeofExpression helper method");
    			}
    			type = (symtab.Type)sym; 
    		}
    		
    		
    		
    		
    	}
		return type;
	}

	public void visit(TypeDeclarationStmt n, Object arg) {
        n.getTypeDeclaration().accept(this, arg);
    }

    public void visit(AssertStmt n, Object arg) {
        n.getCheck().accept(this, arg);
        if (n.getMessage() != null) {
            n.getMessage().accept(this, arg);
        }
    }

    public void visit(BlockStmt n, Object arg) {
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
            }
        }
    }

    public void visit(LabeledStmt n, Object arg) {
        n.getStmt().accept(this, arg);
    }

    public void visit(EmptyStmt n, Object arg) {
    }

    public void visit(ExpressionStmt n, Object arg) {
        n.getExpression().accept(this, arg);
    }

    public void visit(SwitchStmt n, Object arg) {
        n.getSelector().accept(this, arg);
        if (n.getEntries() != null) {
            for (SwitchEntryStmt e : n.getEntries()) {
                e.accept(this, arg);
            }
        }
    }

    public void visit(SwitchEntryStmt n, Object arg) {
        if (n.getLabel() != null) {
            n.getLabel().accept(this, arg);
        } else {
        }
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
            }
        }
    }

    public void visit(BreakStmt n, Object arg) {
        if (n.getId() != null) {
        }
    }

    public void visit(ReturnStmt n, Object arg) {
        if (n.getExpr() != null) {
            n.getExpr().accept(this, arg);
        }
    }

    public void visit(EnumDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);

        if (n.getImplements() != null) {
            for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
            }
        }

        if (n.getEntries() != null) {
            for (Iterator<EnumConstantDeclaration> i = n.getEntries().iterator(); i.hasNext();) {
                EnumConstantDeclaration e = i.next();
                e.accept(this, arg);
            }
        }
        if (n.getMembers() != null) {
            printMembers(n.getMembers(), arg);
        }
    }

    public void visit(EnumConstantDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);

        if (n.getArgs() != null) {
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
            }
        }
        if (n.getClassBody() != null) {
            printMembers(n.getClassBody(), arg);
        }
    }

    public void visit(EmptyMemberDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
    }

    public void visit(InitializerDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        n.getBlock().accept(this, arg);
    }

    public void visit(IfStmt n, Object arg) {
        n.getCondition().accept(this, arg);
        n.getThenStmt().accept(this, arg);
        if (n.getElseStmt() != null) {
            n.getElseStmt().accept(this, arg);
        }
    }

    public void visit(WhileStmt n, Object arg) {
        n.getCondition().accept(this, arg);
        n.getBody().accept(this, arg);
    }

    public void visit(ContinueStmt n, Object arg) {
    }

    public void visit(DoStmt n, Object arg) {
        n.getBody().accept(this, arg);
        n.getCondition().accept(this, arg);
    }

    public void visit(ForeachStmt n, Object arg) {
    	Expression e = n.getIterable();
    	if (e instanceof MethodCallExpr && ((MethodCallExpr)e).getName().equals("entrySet")) {
    		List<Type> types = ((ClassOrInterfaceType)((ReferenceType)n.getVariable().getType()).getType()).getTypeArgs();
    		Symbol t = n.getCurrentScope().resolve(((MethodCallExpr)n.getIterable()).getScope().toString());
    		String[] typeStrings = t.getType().getName().split(",");
    		if (!types.get(0).toString().equals(typeStrings[0])) {
				throw new A2SemanticsException(("Type " + types.get(0) + " does not match Type " + typeStrings[0] + " on line " + n.getBeginLine()));
    		} else if (!types.get(1).toString().equals(typeStrings[1])) {
				throw new A2SemanticsException(("Type " + types.get(1) + " does not match Type " + typeStrings[1] + " on line " + n.getBeginLine()));
    		}
    	}
        n.getVariable().accept(this, arg);
        n.getIterable().accept(this, arg);
        n.getBody().accept(this, arg);
    }

    public void visit(ForStmt n, Object arg) {
        if (n.getInit() != null) {
            for (Iterator<Expression> i = n.getInit().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
            }
        }
        if (n.getCompare() != null) {
            n.getCompare().accept(this, arg);
        }
        if (n.getUpdate() != null) {
            for (Iterator<Expression> i = n.getUpdate().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
            }
        }
        n.getBody().accept(this, arg);
    }

    public void visit(ThrowStmt n, Object arg) {
        n.getExpr().accept(this, arg);
    }

    public void visit(SynchronizedStmt n, Object arg) {
        n.getExpr().accept(this, arg);
        n.getBlock().accept(this, arg);
    }

    public void visit(TryStmt n, Object arg) {
        n.getTryBlock().accept(this, arg);
        if (n.getCatchs() != null) {
            for (CatchClause c : n.getCatchs()) {
                c.accept(this, arg);
            }
        }
        if (n.getFinallyBlock() != null) {
            n.getFinallyBlock().accept(this, arg);
        }
    }

    public void visit(CatchClause n, Object arg) {
        n.getExcept().accept(this, arg);
        n.getCatchBlock().accept(this, arg);

    }

    public void visit(AnnotationDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);

        if (n.getMembers() != null) {
            printMembers(n.getMembers(), arg);
        }
    }

    public void visit(AnnotationMemberDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);

        n.getType().accept(this, arg);
        if (n.getDefaultValue() != null) {
            n.getDefaultValue().accept(this, arg);
        }
    }

    public void visit(MarkerAnnotationExpr n, Object arg) {
        n.getName().accept(this, arg);
    }

    public void visit(SingleMemberAnnotationExpr n, Object arg) {
        n.getName().accept(this, arg);
        n.getMemberValue().accept(this, arg);
    }

    public void visit(NormalAnnotationExpr n, Object arg) {
        n.getName().accept(this, arg);
        for (Iterator<MemberValuePair> i = n.getPairs().iterator(); i.hasNext();) {
            MemberValuePair m = i.next();
            m.accept(this, arg);
        }
    }

    public void visit(MemberValuePair n, Object arg) {
        n.getValue().accept(this, arg);
    }

    public void visit(LineComment n, Object arg) {
    }

    public void visit(BlockComment n, Object arg) {
    }
}
